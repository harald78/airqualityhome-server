package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="measurement")
public class MeasurementEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id", insertable = false, updatable = false)
    private SensorEntity sensorEntity;

    @Column(name = "sensor_id", nullable = false)
    private long sensorId;

    @Column(nullable = false)
    private Instant timestamp;

    public enum Unit {
        CELSIUS, FAHRENHEIT, M_BAR, PERCENT, PPM;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private Double sensorValue;
}
