package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SensorRepository extends RefreshableCRUDRepository<SensorEntity, Long> {

    Optional<List<SensorEntity>> findByUuid(UUID uuid);
    SensorEntity findFirstById(Long id);
}
