package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.rest.dto.SensorTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SensorTypeMapper {

    SensorTypeDto toDto(SensorTypeEntity sensorType);
}
