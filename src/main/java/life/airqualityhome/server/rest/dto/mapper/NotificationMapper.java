package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.rest.dto.NotificationDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface NotificationMapper {

    @Mapping(target = "username", source = "userEntity.username")
    @Mapping(target = "measurementId", source = "measurementEntity.id")
    @Mapping(target = "baseName", source = "measurementEntity.sensorEntity.sensorBaseSensorType.sensorBase.name")
    @Mapping(target = "sensorType", source = "measurementEntity.sensorEntity.sensorBaseSensorType.sensorType.type")
    @Mapping(target = "sensorName", source = "measurementEntity.sensorEntity.sensorBaseSensorType.sensorType.name")
    @Mapping(target = "location", source = "measurementEntity.sensorEntity.location")
    @Mapping(target = "timestamp", source = "measurementEntity.timestamp")
    NotificationDto toDto(NotificationEntity notification);
}
