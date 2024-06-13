package life.airqualityhome.server.repositories;

import life.airqualityhome.server.model.MeasurementViolationEntity;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementViolationRepository extends RefreshableCRUDRepository<MeasurementViolationEntity, Long> {
}
