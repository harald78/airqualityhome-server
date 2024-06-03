package life.airqualityhome.server.service.measurement;

import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.mapper.BaseRawDataDto;

import java.util.List;

public interface MeasurementService {
    List<LatestMeasurementDto> getUserMeasurements(String userId);

    boolean addMeasurements(BaseRawDataDto rawDataDto);
}
