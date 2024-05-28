package life.airqualityhome.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class MeasurementEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private SensorEntity sensorEntity;

    @Column(nullable = false)
    private Instant timestamp;

    private enum Unit {
        CELSIUS, FAHRENHEIT, M_BAR, PERCENT, PPM;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private Double value;
}
