package life.airqualityhome.server.model.mapper;

import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.rest.dto.UserRequestDto;
import life.airqualityhome.server.rest.dto.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntity toEntity(UserRequestDto requestDto);

    UserResponseDto toResponseDto(UserEntity entity);
}
