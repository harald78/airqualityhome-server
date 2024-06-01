package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.rest.dto.SensorDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface SensorMapper {
    SensorDto toDto(SensorEntity sensor);
}
