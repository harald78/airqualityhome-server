package life.airqualityhome.server.service.measurement;

import life.airqualityhome.server.rest.dto.SensorMeasurementDto;
import java.util.List;

public interface MeasurementService {
    List<SensorMeasurementDto> getUserMeasurements(String userId);
}
