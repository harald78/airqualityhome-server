package life.airqualityhome.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="measurement_violation")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementViolationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "measurement_id", nullable = false, insertable = false, updatable = false)
    private MeasurementEntity measurementEntity;

    @Column(name = "measurement_id", nullable = false)
    private Long measurementEntityId;

    @ManyToOne
    @JoinColumn(name = "sensor_id", insertable = false, updatable = false)
    private SensorEntity sensorEntity;

    @Column(name = "sensor_id", nullable = false)
    private long sensorId;

    public enum Type {
        MAX, MIN;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeasurementViolationEntity.Type type;

    @Column(nullable = false)
    private double alarmValue;

    @Column(nullable = false)
    private double sensorValue;

    @Column(nullable = false)
    private double correctedValue;

}


