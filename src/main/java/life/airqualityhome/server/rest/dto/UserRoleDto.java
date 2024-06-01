package life.airqualityhome.server.rest.dto;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    private String name;
}
