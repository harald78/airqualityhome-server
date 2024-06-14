package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.RegisterRequestEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRequestRepository extends RefreshableCRUDRepository<RegisterRequestEntity, Long> {

    RegisterRequestEntity findFirstById(Long id);

    Optional<RegisterRequestEntity> findByUserIdAndActiveTrue(Long userId);

    Optional<List<RegisterRequestEntity>> findAllByUserId(Long userId);
}
