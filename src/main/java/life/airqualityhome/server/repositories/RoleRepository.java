package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.UserRoleEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends RefreshableCRUDRepository<UserRoleEntity, Long> {
}
