package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="push_subscription")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PushSubscriptionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint;

    private String publicKey;

    private String auth;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;
}
