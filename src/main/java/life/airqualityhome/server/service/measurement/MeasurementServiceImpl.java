package life.airqualityhome.server.service.measurement;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.repositories.MeasurementRepository;
import life.airqualityhome.server.repositories.SensorRepository;
import life.airqualityhome.server.rest.dto.SensorMeasurementDto;
import org.springframework.stereotype.Service;

import java.util.List;
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
            MeasurementEntity measurement = measurementRepository.findTopBySensorEntityOrderByTimestampDesc(sensor);
            if (measurement != null) {
                SensorMeasurementDto dto = new SensorMeasurementDto();
                dto.setUuid(sensor.getUuid().toString());
                dto.setMeasurementId(measurement.getId());
                dto.setSensorType(sensor.getSensorBaseSensorType().getSensorType().toString());
                dto.setSensorTypeName(sensor.getSensorBaseSensorType().getSensorType().getName());
                dto.setLocation(sensor.getLocation());
                dto.setAlarmMax(sensor.getAlarmMax());
                dto.setAlarmMin(sensor.getAlarmMin());
                dto.setTimestamp(measurement.getTimestamp());
                dto.setUnit(measurement.getUnit().name());
                dto.setValue(measurement.getValue());
                return dto;
            }
            return null;
        }).filter(dto -> dto != null).collect(Collectors.toList());
    }
}
