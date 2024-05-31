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
