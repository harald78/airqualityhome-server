package life.airqualityhome.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class NotitficationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "measurement_id", nullable = false)
    private MeasurementEntity measurementEntity;

    private String message;

    private Boolean read;

    private Instant readAt;

    @Column(nullable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant updated;
}
