package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.rest.dto.MeasurementDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "string")
public interface MeasurementMapper {
    MeasurementDto toDto(MeasurementEntity measurement);
}
