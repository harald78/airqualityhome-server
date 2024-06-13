package life.airqualityhome.server.model.event;

import life.airqualityhome.server.model.MeasurementViolationEntity;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class MeasurementViolationEvent {
    List<MeasurementViolationEntity> violationEntities;
    Instant timestamp;
}
