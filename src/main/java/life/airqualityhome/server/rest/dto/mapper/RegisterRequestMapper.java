package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.RegisterRequestEntity;
import life.airqualityhome.server.rest.dto.RegisterRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {
    RegisterRequestDto toDto(RegisterRequestEntity registerRequest);
}
