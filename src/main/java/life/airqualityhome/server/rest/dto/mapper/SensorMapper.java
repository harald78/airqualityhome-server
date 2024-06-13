package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.rest.dto.SensorDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface SensorMapper {

    @Mapping(target = "sensorBase", source = "sensor.sensorBaseSensorType.sensorBase.name")
    @Mapping(target = "sensorType", source = "sensor.sensorBaseSensorType.sensorType.type")
    @Mapping(target = "sensorName", source = "sensor.sensorBaseSensorType.sensorType.name")
    SensorDto toDto(SensorEntity sensor);

}
