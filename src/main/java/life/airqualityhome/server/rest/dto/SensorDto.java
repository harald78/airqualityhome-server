package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDto {
    private long id;
    private String uuid;
    private Long sensorBaseSensorTypeId;
    private Long userId;
    private String location;
    private double alarmMax;
    private double alarmMin;
    private boolean alarmActive;
    private double warningThreshold;
    private double linearCorrectionValue;
}
