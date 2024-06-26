package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.PushSubscriptionEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;

import java.util.List;

public interface PushSubscriptionRepository extends RefreshableCRUDRepository<PushSubscriptionEntity, Long> {
    List<PushSubscriptionEntity> findByUserId(Long userId);
}
