package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sensor_base_sensor_types")
public class SensorBaseSensorTypeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_base_entity_id", nullable = false)
    private Long sensorBaseEntityId;


    @Column(name = "sensor_types_id", nullable = false)
    private Long sensorTypesId;

    @ManyToOne
    @JoinColumn(name = "sensor_base_entity_id", insertable = false, updatable = false)
    private SensorBaseEntity sensorBase;

    @ManyToOne
    @JoinColumn(name = "sensor_types_id", insertable = false, updatable = false)
    private SensorTypeEntity sensorType;
}
