package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorBaseSensorTypeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_base_id", nullable = false)
    private Long sensorBaseId;


    @Column(name = "sensor_type_id", nullable = false)
    private Long sensorTypeId;

    @ManyToOne
    @JoinColumn(name = "sensor_base_id", insertable = false, updatable = false)
    private SensorBaseEntity sensorBase;

    @ManyToOne
    @JoinColumn(name = "sensor_type_id", insertable = false, updatable = false)
    private SensorTypeEntity sensorType;
}
