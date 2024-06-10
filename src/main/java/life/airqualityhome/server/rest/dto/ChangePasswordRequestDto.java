package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChangePasswordRequestDto {
    private Long id;
    private String username;
    private String oldPassword;
    private String password;
}
