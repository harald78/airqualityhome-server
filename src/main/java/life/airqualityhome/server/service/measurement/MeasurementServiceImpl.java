package life.airqualityhome.server.service.measurement;

import life.airqualityhome.server.config.ApplicationProperties;
import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.MeasurementViolationEntity;
import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.model.event.MeasurementViolationEvent;
import life.airqualityhome.server.repositories.MeasurementRepository;
import life.airqualityhome.server.repositories.MeasurementViolationRepository;
import life.airqualityhome.server.rest.dto.HistoryMeasurementDto;
import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.SensorRawDataDto;
import life.airqualityhome.server.rest.dto.BaseRawDataDto;
import life.airqualityhome.server.rest.exceptions.NoContentFoundException;
import life.airqualityhome.server.service.SensorService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MeasurementServiceImpl implements MeasurementService {


    private final SensorService sensorService;
    private final MeasurementRepository measurementRepository;
    private final MeasurementViolationRepository measurementViolationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ApplicationProperties applicationProperties;

    public MeasurementServiceImpl(final SensorService sensorService, final MeasurementRepository measurementRepository,
                                  final MeasurementViolationRepository measurementViolationRepository,
                                  final ApplicationEventPublisher applicationEventPublisher,
                                  final ApplicationProperties applicationProperties) {
        this.sensorService = sensorService;
        this.measurementRepository = measurementRepository;
        this.measurementViolationRepository = measurementViolationRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.applicationProperties = applicationProperties;

    }

    @Override
    public List<LatestMeasurementDto> getUserMeasurements(String userId) {
        try {
            List<SensorEntity> sensors = sensorService.getSensorEntitiesForUser(Long.parseLong(userId));
            return sensors.stream().map(sensor -> {
                Optional<MeasurementEntity> measurement = measurementRepository.findTopBySensorEntityOrderByTimestampDesc(sensor);
                if (measurement.isPresent()) {
                    var sensorMeasurement = measurement.get();
                    LatestMeasurementDto dto = new LatestMeasurementDto();
                    dto.setId(sensorMeasurement.getId());
                    dto.setLocation(sensor.getLocation());
                    dto.setSensorId(sensor.getId());
                    dto.setUuid(sensor.getUuid().toString());
                    dto.setSensorBaseName(sensor.getSensorBaseSensorType().getSensorBase().getName());
                    dto.setSensorName(sensor.getSensorBaseSensorType().getSensorType().getName());
                    dto.setSensorType(sensor.getSensorBaseSensorType().getSensorType().getType().name());
                    dto.setLocation(sensor.getLocation());
                    dto.setAlarmMax(sensor.getAlarmMax());
                    dto.setAlarmMin(sensor.getAlarmMin());
                    dto.setTimestamp(sensorMeasurement.getTimestamp());
                    dto.setUnit(sensorMeasurement.getUnit().name());
                    dto.setValue(sensorMeasurement.getSensorValue());
                    dto.setAlarmActive(sensor.isAlarmActive());
                    dto.setWarningThreshold(sensor.getWarningThreshold());
                    dto.setLinearCorrectionValue(sensor.getLinearCorrectionValue());
                    return dto;
                }
                return null;
            }).filter(Objects::nonNull).toList();

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user id");
        }
    }

    @Override
    public boolean addMeasurements(final BaseRawDataDto baseRawDataDto) {
        List<SensorEntity> sensorEntities = this.sensorService.getAllSensorEntitiesByUUID(baseRawDataDto.getId());
        if (sensorEntities.isEmpty()) {
            return false;
        }

        List<MeasurementEntity> measurementEntities = baseRawDataDto.getMeasurements().stream()
                .map(m -> this.getMeasurementEntity(m, sensorEntities, baseRawDataDto.getTimestamp(), baseRawDataDto.getId())).toList();
        Iterable<MeasurementEntity> savedMeasurements = this.measurementRepository.saveAll(measurementEntities);

        // Check if measurement has an alarm violation and save the violations to database
        List<MeasurementViolationEntity> measurementViolationEntities = this.checkMeasurementsForSensorAlarmNotifications(measurementEntities, sensorEntities);
        if (!measurementViolationEntities.isEmpty()) {
            var violationsIterator = this.measurementViolationRepository.saveAll(measurementViolationEntities);
            var savedViolations = StreamSupport.stream(violationsIterator.spliterator(), false).toList();
            var violationEvent = MeasurementViolationEvent.builder()
                .violationEntities(savedViolations)
                .timestamp(Instant.now())
                .build();
            this.applicationEventPublisher.publishEvent(violationEvent);
        }

        return Objects.equals(StreamSupport.stream(savedMeasurements.spliterator(), false).toList().size(),
            sensorEntities.size());
    }

    public List<MeasurementViolationEntity> checkMeasurementsForSensorAlarmNotifications(final List<MeasurementEntity> measurementEntities, final List<SensorEntity> sensorEntities) {
        return sensorEntities.stream().filter(SensorEntity::isAlarmActive)
            .flatMap(s -> measurementEntities.stream().filter(me -> me.getSensorId() == s.getId())
                .filter(me -> this.measurementViolation.test(me, s))
                .map(me -> this.getMeasurementViolation(me, s))).toList();
    }

    BiPredicate<MeasurementEntity, SensorEntity> measurementViolation = (measurementEntity, sensor) ->
        this.getCorrectedMeasurementValue(measurementEntity.getSensorValue(), sensor.getLinearCorrectionValue()) > sensor.getAlarmMax() ||
            this.getCorrectedMeasurementValue(measurementEntity.getSensorValue(), sensor.getLinearCorrectionValue()) < sensor.getAlarmMin();

    protected double getCorrectedMeasurementValue(final double measuredValue, final double linearCorrectionValue) {
        return measuredValue + linearCorrectionValue;
    }

    protected MeasurementViolationEntity getMeasurementViolation(MeasurementEntity measurementEntity, SensorEntity sensor) {
        MeasurementViolationEntity.Type type = MeasurementViolationEntity.Type.MIN;
        var alarmValue = sensor.getAlarmMin();

        if (getCorrectedMeasurementValue(measurementEntity.getSensorValue(), sensor.getLinearCorrectionValue()) > sensor.getAlarmMax()) {
            type = MeasurementViolationEntity.Type.MAX;
            alarmValue = sensor.getAlarmMax();
        }

        return MeasurementViolationEntity
            .builder()
            .measurementEntityId(measurementEntity.getId())
            .sensorId(sensor.getId())
            .userId(sensor.getUserId())
            .correctedValue(getCorrectedMeasurementValue(measurementEntity.getSensorValue(), sensor.getLinearCorrectionValue()))
            .sensorValue(measurementEntity.getSensorValue())
            .alarmValue(alarmValue)
            .type(type)
            .build();
    }

    @Override
    public HistoryMeasurementDto getBaseMeasurements(final Long userId, final Long baseId, final Instant from, final Instant to) {
        List<SensorEntity> sensorEntities = this.sensorService.getSensorsByBaseIdAndUserId(baseId, userId);
        if (sensorEntities.isEmpty()) {
            throw new NoContentFoundException("No content found");
        }

        HistoryMeasurementDto historyMeasurementDto = new HistoryMeasurementDto();

        sensorEntities.forEach(s -> {
            List<MeasurementEntity> measurementEntities = measurementRepository.findBySensorIdAndTimestampIsBetweenOrderByCreatedAsc(s.getId(), from, to);
            var groupedData = this.getChartDataPoints(measurementEntities);
            var chartDataDto = this.getChartDataList(groupedData, s);
            chartDataDto.forEach(c -> historyMeasurementDto.getData().add(c));

            if (historyMeasurementDto.getBaseId() == null) {
                historyMeasurementDto.setBaseId(s.getSensorBaseSensorType().getSensorBaseEntityId());
                historyMeasurementDto.setBaseName(s.getSensorBaseSensorType().getSensorBase().getName());
                historyMeasurementDto.setLocation(s.getLocation());
            }
        });

        return historyMeasurementDto;
    }

    @Override
    public HistoryMeasurementDto getSensorMeasurements(final Long sensorId, final Instant from, final Instant to) {
        List<MeasurementEntity> measurements = measurementRepository.findBySensorIdAndTimestampIsBetweenOrderByCreatedAsc(sensorId, from, to);
        SensorEntity sensor = this.sensorService.getSensorEntityById(sensorId);

        Map<String, List<HistoryMeasurementDto.ChartDataPoint>> groupedData = this.getChartDataPoints(measurements);
        List<HistoryMeasurementDto.ChartDataDto> chartDataDto = this.getChartDataList(groupedData, sensor);
        return new HistoryMeasurementDto(sensor.getSensorBaseSensorType().getSensorBaseEntityId(),
                sensor.getSensorBaseSensorType().getSensorBase().getName(), sensor.getLocation(), chartDataDto);
    }

    protected Map<String, List<HistoryMeasurementDto.ChartDataPoint>> getChartDataPoints(final List<MeasurementEntity> measurementEntities) {
        if (measurementEntities.isEmpty()) {
            return new HashMap<>();
        }

        AtomicReference<Instant> created = new AtomicReference<>(measurementEntities.get(0).getCreated());
        var firstId = measurementEntities.get(0).getId();
        var lastId = measurementEntities.get(measurementEntities.size() - 1).getId();
        return measurementEntities.stream()
            .filter(m -> {
                if (firstId == m.getId() || lastId == m.getId() || m.getCreated().isAfter(created.get().plus(this.applicationProperties.getMaxSensorMeasurementIntervalMinutes(), ChronoUnit.MINUTES))) {
                    created.set(m.getCreated());
                    return true;
                }
                return false; }).collect(Collectors.groupingBy(
                               m -> m.getUnit().name(),
                               Collectors.mapping(
                                   m -> new HistoryMeasurementDto.ChartDataPoint(m.getSensorValue(), m.getTimestamp().atOffset(ZoneOffset.UTC).toLocalDateTime()),
                                   Collectors.toList()
                               )));
    }

    protected List<HistoryMeasurementDto.ChartDataDto> getChartDataList(final Map<String, List<HistoryMeasurementDto.ChartDataPoint>> groupedData, SensorEntity sensor) {
        return groupedData.entrySet().stream()
                   .map(entry -> new HistoryMeasurementDto.ChartDataDto(sensor.getSensorBaseSensorType().getSensorType().getType().name(),
                                                                        sensor.getSensorBaseSensorType().getSensorType().getName(),
                                                                        sensor.getSensorBaseSensorType().getSensorType().getType().name() + " (" + sensor.getSensorBaseSensorType().getSensorType().getName() + ")",
                                                                        entry.getKey(),
                                                                        sensor.getAlarmMin(), sensor.getAlarmMax(), sensor.getSensorBaseSensorType().getSensorType().getMinValue(),
                                                                        sensor.getSensorBaseSensorType().getSensorType().getMaxValue(),
                                                                        entry.getValue())).toList();
    }

    MeasurementEntity getMeasurementEntity(SensorRawDataDto rawDataDto, List<SensorEntity> sensorEntities, Instant timestamp, String id) throws IllegalStateException {
        Optional<SensorEntity> sensor = this.filterBySensorType(sensorEntities, rawDataDto.getType());
        if (sensor.isEmpty()) {
            throw new IllegalStateException("Sensor type " + rawDataDto.getType() + " not found for base " + id);
        }
        return MeasurementEntity.builder()
                .sensorValue(rawDataDto.getValue())
                .unit(rawDataDto.getUnit())
                .sensorEntity(sensor.get())
                .sensorId(sensor.get().getId())
                .timestamp(timestamp).build();
    }

    Optional<SensorEntity> filterBySensorType(List<SensorEntity> sensorEntities, SensorTypeEntity.Type sensorType) {
        if (sensorEntities == null || sensorEntities.isEmpty()) {
            return Optional.empty();
        }
        return sensorEntities.stream()
                .filter(s -> s.getSensorBaseSensorType().getSensorType().getType().equals(sensorType))
                .findFirst();
    }

}