package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.RegisterRequestEntity;
import life.airqualityhome.server.rest.dto.RegisterRequestDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface RegisterRequestMapper {

    @Mapping(target = "sensorBaseId", source = "sensorBase.id")
    RegisterRequestDto toDto(RegisterRequestEntity registerRequest);
}
