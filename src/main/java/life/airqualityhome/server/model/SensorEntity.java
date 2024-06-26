package life.airqualityhome.server.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="sensor")
public class SensorEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "sensor_base_sensor_type_id", insertable = false, updatable = false)
    private SensorBaseSensorTypeEntity sensorBaseSensorType;

    @Column(name = "sensor_base_sensor_type_id", nullable = false)
    private Long sensorBaseSensorTypeId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "location", nullable = false)
    private String location;

    @Builder.Default
    @Column(name = "alarm_max")
    private Double alarmMax = 0.0;

    @Builder.Default
    @Column(name = "alarm_min")
    private Double alarmMin = 0.0;

    @Builder.Default
    @Column(name = "alarm_active")
    private boolean alarmActive = false;

    @Builder.Default
    @Column(name = "warning_threshold")
    private Double warningThreshold = 0.0;

    @Builder.Default
    @Column(name = "linear_correction_value")
    private Double linearCorrectionValue = 0.0;

}
