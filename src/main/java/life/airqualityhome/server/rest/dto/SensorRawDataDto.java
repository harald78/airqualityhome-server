package life.airqualityhome.server.rest.dto;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorRawDataDto implements Serializable {
    MeasurementEntity.Unit unit;
    SensorTypeEntity.Type type;
    Double value;
}
