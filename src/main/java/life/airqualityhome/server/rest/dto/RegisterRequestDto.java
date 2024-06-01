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
public class RegisterRequestDto implements Serializable {

    private Long id;

    private Long userId;

    private Long sensorBaseId;

    private Boolean active;

    private Boolean canceled;

    private String location;
}
