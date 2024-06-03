package life.airqualityhome.server.rest.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseDto implements Serializable {

    private Long id;
    private String username;
    private String email;
    private Set<UserRoleDto> roles;
}
