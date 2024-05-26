package life.airqualityhome.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class SensorTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private enum Type {
        TEMPERATURE, HUMIDITY, PRESSURE, GAS, PARTICLE, LIGHT;
    }

    @Column(nullable = false)
    private Double maxValue;

    @Column(nullable = false)
    private Double minValue;

    @Column(nullable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant updated;
}
