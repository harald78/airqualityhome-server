package life.airqualityhome.server.service;

import life.airqualityhome.server.model.*;
import life.airqualityhome.server.repositories.SensorBaseSensorTypeRepository;
import life.airqualityhome.server.repositories.SensorRepository;
import life.airqualityhome.server.rest.dto.SensorDto;
import life.airqualityhome.server.rest.dto.mapper.SensorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class SensorService {


    private final SensorBaseSensorTypeRepository sensorBaseSensorTypeRepository;

    private final SensorRepository sensorRepository;

    private final SensorMapper sensorMapper;

    public SensorService(SensorBaseSensorTypeRepository sensorBaseSensorTypeRepository, SensorRepository sensorRepository, SensorMapper sensorMapper) {
        this.sensorBaseSensorTypeRepository = sensorBaseSensorTypeRepository;
        this.sensorRepository = sensorRepository;
        this.sensorMapper = sensorMapper;
    }

    public SensorEntity getSensorById(Long id) {
        return sensorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
    }

    public List<SensorEntity> getAllSensorEntitiesByUUID(String sensorId) {
        UUID uuid = UUID.nameUUIDFromBytes(sensorId.getBytes());
        return sensorRepository.findByUuid(uuid).orElse(new ArrayList<>());
    }

    public List<SensorDto> getAllSensorsByUUID(String sensorId) {
        var sensorList = this.getAllSensorEntitiesByUUID(sensorId);
        return sensorList.stream().map(sensorMapper::toDto).toList();
    }

    public List<SensorEntity> getSensorEntitiesForUser(Long userId) {
        return this.sensorRepository.findByUserEntityId(userId)
                                           .orElse(new ArrayList<SensorEntity>());
    }

    public List<SensorDto> getSensorsForUser(Long userId) {
       return this.getSensorEntitiesForUser(userId)
           .stream().map(sensorMapper::toDto).toList();
    }

    public List<SensorDto> registerSensorsForUser(RegisterRequestEntity registrationRequest, Long userId, String sensorId) {
        // Lade SensorBase with SensorTypes
        List<Long> sensorIds = registrationRequest.getSensorBase().getSensorTypes()
                .stream().map(SensorTypeEntity::getId).toList();
        List<SensorBaseSensorTypeEntity> sensorBaseSensorTypeRelations =
                sensorBaseSensorTypeRepository.findAllBySensorBaseIdAndSensorTypeIdIn(registrationRequest.getSensorBase().getId(), sensorIds);
        List<SensorEntity> sensorEntities = new ArrayList<>();

        UUID uuid = UUID.nameUUIDFromBytes(sensorId.getBytes());


        for (SensorBaseSensorTypeEntity sensorBaseSensorTypeEntity : sensorBaseSensorTypeRelations) {
            var sensor = SensorEntity.builder()
                    .sensorBaseSensorTypeId(sensorBaseSensorTypeEntity.getId())
                    .userId(userId)
                    .uuid(uuid)
                    .location(registrationRequest.getLocation())
                    .alarmMax(0.0)
                    .alarmMin(0.0)
                    .alarmActive(false)
                    .build();
            sensorEntities.add(sensor);
        }
        Iterable<SensorEntity> savedSensors = sensorRepository.saveAll(sensorEntities);
        return StreamSupport.stream(savedSensors.spliterator(), false)
                .map(sensorMapper::toDto).toList();
    }


}
