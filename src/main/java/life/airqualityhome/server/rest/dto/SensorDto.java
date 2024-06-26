package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDto {
    private long id;
    private String uuid;
    private String sensorBase;
    private String sensorType;
    private String sensorName;
    private Long sensorBaseSensorTypeId;
    private Long userId;
    private String location;
    private double alarmMax;
    private double alarmMin;
    private boolean alarmActive;
    private double warningThreshold;
    private double linearCorrectionValue;
    private Instant updated;
    private Instant created;
}
