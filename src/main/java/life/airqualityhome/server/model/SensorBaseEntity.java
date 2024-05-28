package life.airqualityhome.server.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class SensorBaseEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
