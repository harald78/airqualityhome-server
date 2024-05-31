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

    @JoinColumn(name = "sensor_base_entity_id", nullable = false)
    private Long sensorBaseId;


    @JoinColumn(name = "sensor_types_id", nullable = false)
    private Long sensorTypeId;
}
