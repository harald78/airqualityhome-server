package life.airqualityhome.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="sensor_base_sensor_type")
public class SensorBaseSensorTypeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_base_id", nullable = false)
    private SensorBaseEntity sensorBase;

    @ManyToOne
    @JoinColumn(name = "sensor_type_id", nullable = false)
    private SensorTypeEntity sensorType;
}
