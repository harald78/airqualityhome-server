package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.rest.dto.SensorTypeDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface SensorTypeMapper {

    SensorTypeDto toDto(SensorTypeEntity sensorTypeEntity);
}

