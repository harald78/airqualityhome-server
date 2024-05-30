package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends RefreshableCRUDRepository<NotificationEntity, Long> {

    NotificationEntity findFirstById(Long id);
}
