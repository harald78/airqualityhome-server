package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorTypeDto implements Serializable {

    private Long id;

    private String name;

    private String type;

    private Double maxValue;

    private Double minValue;
}
