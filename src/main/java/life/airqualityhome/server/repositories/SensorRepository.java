package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SensorRepository extends RefreshableCRUDRepository<SensorEntity, Long> {

    SensorEntity findByUuid(UUID uuid);
    SensorEntity findFirstById(Long id);
}
