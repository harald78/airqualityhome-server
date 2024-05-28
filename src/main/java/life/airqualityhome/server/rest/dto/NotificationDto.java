package life.airqualityhome.server.rest.dto;

import java.time.Instant;

public class NotificationDto {

    private Long id;

    private Long userId;

    private Long measurementId;

    private String message;

    private Boolean read;

    private Instant readAt;
}
