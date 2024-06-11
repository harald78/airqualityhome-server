package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name="notification")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "measurement_id", nullable = false)
    private MeasurementEntity measurementEntity;

    private String message;

    private Boolean read;

    private Instant readAt;

}