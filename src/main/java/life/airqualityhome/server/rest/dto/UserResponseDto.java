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
public class UserResponseDto {

    private Long id;
    private String username;
    private Set<UserRoleDto> roles;
}
