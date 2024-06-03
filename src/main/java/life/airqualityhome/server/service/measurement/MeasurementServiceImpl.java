package life.airqualityhome.server.service.measurement;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.repositories.MeasurementRepository;
import life.airqualityhome.server.repositories.SensorRepository;
import life.airqualityhome.server.rest.dto.SensorMeasurementDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeasurementServiceImpl implements MeasurementService {


    private final SensorRepository sensorRepository;


    private final MeasurementRepository measurementRepository;

    public MeasurementServiceImpl(SensorRepository sensorRepository, MeasurementRepository measurementRepository) {
        this.sensorRepository = sensorRepository;
        this.measurementRepository = measurementRepository;

    }

    @Override
    public List<SensorMeasurementDto> getUserMeasurements(String userId) {
        List<SensorEntity> sensors = sensorRepository.findByUserEntityId(Long.parseLong(userId));

        return sensors.stream().map(sensor -> {
            Optional<MeasurementEntity> measurement = measurementRepository.findTopBySensorEntityOrderByTimestampDesc(sensor);
            if (measurement.isPresent()) {
                var sensorMeasurement = measurement.get();
                SensorMeasurementDto dto = new SensorMeasurementDto();
                dto.setUuid(sensor.getUuid().toString());
                dto.setMeasurementId(sensorMeasurement.getId());
                dto.setSensorBaseName(sensor.getSensorBaseSensorType().getSensorBase().getName());
                dto.setSensorName(sensor.getSensorBaseSensorType().getSensorType().getName());
                dto.setSensorType(sensor.getSensorBaseSensorType().getSensorType().getType().name());
                dto.setLocation(sensor.getLocation());
                dto.setAlarmMax(sensor.getAlarmMax());
                dto.setAlarmMin(sensor.getAlarmMin());
                dto.setTimestamp(sensorMeasurement.getTimestamp());
                dto.setUnit(sensorMeasurement.getUnit().name());
                dto.setValue(sensorMeasurement.getValue());
                return dto;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
