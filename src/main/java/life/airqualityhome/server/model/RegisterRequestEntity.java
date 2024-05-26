package life.airqualityhome.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class RegisterRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "sensor_base_id", nullable = false)
    private SensorBaseEntity sensorBase;

    private Boolean active;

    @Column(nullable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant updated;
}
