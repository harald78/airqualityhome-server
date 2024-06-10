package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.rest.dto.MeasurementDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "string", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface MeasurementMapper {
    MeasurementDto toDto(MeasurementEntity measurement);
}
