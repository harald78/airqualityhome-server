package life.airqualityhome.server.rest.dto.mapper;


import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.rest.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity user);

}
