package life.airqualityhome.server.service.measurement;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.repositories.MeasurementRepository;
import life.airqualityhome.server.rest.dto.HistoryMeasurementDto;
import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.SensorRawDataDto;
import life.airqualityhome.server.rest.dto.BaseRawDataDto;
import life.airqualityhome.server.service.SensorService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MeasurementServiceImpl implements MeasurementService {


    private final SensorService sensorService;
    private final MeasurementRepository measurementRepository;



    public MeasurementServiceImpl(SensorService sensorService, MeasurementRepository measurementRepository) {
        this.sensorService = sensorService;
        this.measurementRepository = measurementRepository;
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
    public boolean addMeasurements(BaseRawDataDto baseRawDataDto) {
        List<SensorEntity> sensorEntities = this.sensorService.getAllSensorEntitiesByUUID(baseRawDataDto.getId());
        if (sensorEntities.isEmpty()) {
            return false;
        }

        List<MeasurementEntity> measurementEntities = baseRawDataDto.getMeasurements().stream()
                .map(m -> this.getMeasurementEntity(m, sensorEntities, baseRawDataDto.getTimestamp(), baseRawDataDto.getId())).toList();
        Iterable<MeasurementEntity> savedMeasurements = this.measurementRepository.saveAll(measurementEntities);
        return Objects.equals(StreamSupport.stream(savedMeasurements.spliterator(), false).toList().size(),
            sensorEntities.size());
    }

    @Override
    public HistoryMeasurementDto getSensorMeasurements(Long sensorId, Instant from, Instant to) {
        List<MeasurementEntity> measurements = measurementRepository.findBySensorIdAndTimestampIsBetween(sensorId, from, to);
        SensorEntity sensor = this.sensorService.getSensorById(sensorId);

        Map<String, List<HistoryMeasurementDto.ChartDataPoint>> groupedData = measurements.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getUnit().name(),
                        Collectors.mapping(
                                m -> new HistoryMeasurementDto.ChartDataPoint(m.getSensorValue(), m.getTimestamp().atOffset(ZoneOffset.UTC).toLocalDateTime()),
                                Collectors.toList()
                        )
                ));

        List<HistoryMeasurementDto.ChartDataDto> chartDataDto = groupedData.entrySet().stream()
                .map(entry -> new HistoryMeasurementDto.ChartDataDto(sensor.getSensorBaseSensorType().getSensorType().getType().name(),
                        sensor.getSensorBaseSensorType().getSensorType().getName(), sensor.getSensorBaseSensorType().getSensorType().getType().name(),
                        entry.getKey(),
                        sensor.getAlarmMin(), sensor.getAlarmMax(), sensor.getSensorBaseSensorType().getSensorType().getMinValue(),
                        sensor.getSensorBaseSensorType().getSensorType().getMaxValue(),
                        entry.getValue()))
                .collect(Collectors.toList());

        return new HistoryMeasurementDto(sensor.getSensorBaseSensorType().getSensorBaseEntityId(),
                sensor.getSensorBaseSensorType().getSensorBase().getName(), sensor.getLocation(), chartDataDto);
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