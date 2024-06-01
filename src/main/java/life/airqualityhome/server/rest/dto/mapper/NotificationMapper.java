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
    NotificationDto toDto(NotificationEntity notification);
}
