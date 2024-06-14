package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sensor_type")
public class SensorTypeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public enum Type {
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
