package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorBaseRepository extends RefreshableCRUDRepository<SensorBaseEntity, Long> {

    SensorBaseEntity findByName(String name);
    SensorBaseEntity findFirstById(Long id);
}
