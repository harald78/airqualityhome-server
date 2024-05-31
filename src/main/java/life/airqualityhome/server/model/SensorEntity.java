package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SensorEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "sensor_base_sensor_type_id", nullable = false)
    private SensorBaseSensorTypeEntity sensorBaseSensorType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    private String location;

    private Double alarmMax;

    private Double alarmMin;
}
