package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationCRUDRepository extends CrudRepository<NotificationEntity, Long>{
    List<NotificationEntity> findAllByUserId(Long userId);
}
