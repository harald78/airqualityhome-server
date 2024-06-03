package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeasurementRepository extends RefreshableCRUDRepository<MeasurementEntity, Long> {

    MeasurementEntity findFirstById(Long id);
    Optional<MeasurementEntity> findTopBySensorEntityOrderByTimestampDesc(SensorEntity sensorEntity);

}

