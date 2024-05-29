package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.RefreshTokenEntity;
import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends RefreshableCRUDRepository<RefreshTokenEntity, Integer> {

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<List<RefreshTokenEntity>> findAllByUserInfo(UserEntity user);

}
