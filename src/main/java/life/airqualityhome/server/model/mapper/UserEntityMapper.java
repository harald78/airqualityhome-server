package life.airqualityhome.server.model.mapper;

import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.rest.dto.ChangeUserRequestDto;
import life.airqualityhome.server.rest.dto.UserResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserEntityMapper {

    UserEntity toEntity(ChangeUserRequestDto requestDto);

    @Mapping(target = "email", source = "email")
    UserResponseDto toResponseDto(UserEntity entity);
}
