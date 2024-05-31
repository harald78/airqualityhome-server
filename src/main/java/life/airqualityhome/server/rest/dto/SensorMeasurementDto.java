package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorMeasurementDto implements Serializable {
    private String uuid;
    private Long measurementId;
    private String sensorType;
    private String sensorTypeName;
    private String location;
    private Double alarmMax;
    private Double alarmMin;
    private Instant timestamp;
    private String unit;
    private Double value;


}
