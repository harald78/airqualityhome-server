package life.airqualityhome.server.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import life.airqualityhome.server.util.CustomInstantDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRawDataDto implements Serializable {
    String id;
    String base;

    @JsonDeserialize(using = CustomInstantDeserializer.class)
    Instant timestamp;

    List<SensorRawDataDto> measurements;
}
