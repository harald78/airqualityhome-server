package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.rest.dto.SensorBaseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface SensorBaseMapper {
    SensorBaseDto toDto(SensorBaseEntity sensorBase);
}
