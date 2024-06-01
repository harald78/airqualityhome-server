package life.airqualityhome.server.rest.dto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseDto {

    private Long id;
    private String username;
    private Set<UserRoleDto> roles;
}
