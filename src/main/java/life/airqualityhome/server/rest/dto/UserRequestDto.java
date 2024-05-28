package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequestDto {

    private Long id;
    private String username;
    private String password;
    private Set<UserRoleDto> roles;


}
