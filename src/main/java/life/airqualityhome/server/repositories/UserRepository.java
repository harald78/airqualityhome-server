package life.airqualityhome.server.repositories;


import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends RefreshableCRUDRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findFirstById(Long id);

}
