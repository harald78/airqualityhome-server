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
public class LatestMeasurementDto implements Serializable {
    private Long id;
    private Long sensorId;
    private String uuid;
    private String sensorBaseName;
    private String sensorName;
    private String sensorType;
    private String location;
    private Double alarmMax;
    private Double alarmMin;
    private Instant timestamp;
    private String unit;
    private Double value;
    private boolean alarmActive;
    private Double warningThreshold;
    private Double linearCorrectionValue;
}
