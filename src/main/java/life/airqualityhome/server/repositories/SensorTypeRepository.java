package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorTypeRepository extends RefreshableCRUDRepository<SensorTypeEntity, Long> {

    SensorTypeEntity findByName(String name);
    SensorTypeEntity findFirstById(Long id);
}
