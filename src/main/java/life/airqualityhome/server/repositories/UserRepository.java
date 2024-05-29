package life.airqualityhome.server.repositories;


import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends RefreshableCRUDRepository<UserEntity, Long> {

    public UserEntity findByUsername(String username);
    UserEntity findFirstById(Long id);

}
