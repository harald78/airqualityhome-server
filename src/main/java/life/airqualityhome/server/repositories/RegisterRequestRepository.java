package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.RegisterRequestEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRequestRepository extends RefreshableCRUDRepository<RegisterRequestEntity, Long> {

    RegisterRequestEntity findFirstById(Long id);
}
