package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.PushSubscriptionEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;

import java.util.Optional;

public interface PushSubscriptionRepository extends RefreshableCRUDRepository<PushSubscriptionEntity, Long> {
    Optional<PushSubscriptionEntity> findByUserId(Long userId);
}
