package life.airqualityhome.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class SensorTypeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private enum Type {
        TEMPERATURE, HUMIDITY, PRESSURE, GAS, PARTICLE, LIGHT;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Double maxValue;

    @Column(nullable = false)
    private Double minValue;

}
