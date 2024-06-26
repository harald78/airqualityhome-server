package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDto implements Serializable {

    private Long id;

    private Long userId;

    private Long measurementId;

    private String message;

    private Boolean acknowledged;

    private Instant readAt;

    private String username;

    private String baseName;

    private String location;

    private String sensorType;

    private String sensorName;

    private Instant timestamp;
}
