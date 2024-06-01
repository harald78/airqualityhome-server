package life.airqualityhome.server.repositories;


import life.airqualityhome.server.model.SensorBaseSensorTypeEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorBaseSensorTypeRepository extends RefreshableCRUDRepository<SensorBaseSensorTypeEntity, Long> {

    List<SensorBaseSensorTypeEntity> findAllBySensorBaseIdAndSensorTypeIdIn(Long sensorBaseId, List<Long> sensorTypeIds);

}
