package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.UserRoleEntity;
import life.airqualityhome.server.rest.dto.UserRoleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    UserRoleDto toDto(UserRoleEntity userRole);

}
