package life.airqualityhome.server.rest.dto;

import java.time.Instant;

public class MeasurementDto {

    private Long id;

    private Long sensorId;

    private Instant timestamp;

    private String unit;

    private Double value;
}
