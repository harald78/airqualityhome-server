package life.airqualityhome.server.rest.dto.mapper;

import life.airqualityhome.server.rest.dto.SensorRawDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRawDataDto implements Serializable {
    String id;
    String base;
    LocalDateTime timestamp;
    List<SensorRawDataDto> measurements;
}
