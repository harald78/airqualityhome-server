package life.airqualityhome.server.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class SensorBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant updated;
}
