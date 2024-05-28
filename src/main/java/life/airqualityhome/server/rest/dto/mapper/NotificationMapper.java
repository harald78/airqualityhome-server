package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.rest.dto.NotificationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "string")
public interface NotificationMapper {

    NotificationDto toDto(NotificationEntity notification);
}
