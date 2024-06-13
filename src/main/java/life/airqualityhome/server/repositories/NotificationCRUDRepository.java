package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface NotificationCRUDRepository extends CrudRepository<NotificationEntity, Long>{
    Optional<List<NotificationEntity>> findAllByUserId(Long userId);
    Optional<NotificationEntity> findByUserIdAndMeasurementEntityIdAndCreatedAfter(Long userId, Long measurementId, Instant timestamp);
}
