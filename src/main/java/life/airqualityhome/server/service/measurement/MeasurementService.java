package life.airqualityhome.server.service.measurement;

import life.airqualityhome.server.rest.dto.HistoryMeasurementDto;
import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.BaseRawDataDto;

import java.time.Instant;
import java.util.List;

public interface MeasurementService {
    List<LatestMeasurementDto> getUserMeasurements(String userId);
    HistoryMeasurementDto getSensorMeasurements(Long sensorId, Instant from, Instant to);

    boolean addMeasurements(BaseRawDataDto rawDataDto);
}
