package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface SensorRepository extends RefreshableCRUDRepository<SensorEntity, Long> {

    SensorEntity findByUuid(UUID uuid);
    SensorEntity findFirstById(Long id);
    List<SensorEntity> findByUserEntityId(Long userId);
}
