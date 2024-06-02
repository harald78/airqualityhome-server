package life.airqualityhome.server.service.measurement;


import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorBaseSensorTypeEntity;
import life.airqualityhome.server.model.SensorEntity;

import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.repositories.MeasurementRepository;
import life.airqualityhome.server.repositories.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceManagerTest {
    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private MeasurementRepository measurementRepository;
    private MeasurementServiceManager sut;


    @BeforeEach
    void setUp() {
        this.sut = new MeasurementServiceManager(sensorRepository, measurementRepository);
    }

    @Test
    void getUserMeasurements() {

        //given
        UUID uuid1 = UUID.nameUUIDFromBytes("F0F0F0".getBytes());

        SensorTypeEntity sensorType = new SensorTypeEntity();
        sensorType.setName("Temperature");

        SensorBaseSensorTypeEntity sensorBaseSensorType = new SensorBaseSensorTypeEntity();
        sensorBaseSensorType.setSensorType(sensorType);

        var sensorList = List.of(
                SensorEntity.builder()
                        .id(1L)
                        .uuid(uuid1)
                        .sensorBaseSensorType(sensorBaseSensorType)
                        .location("Living room")
                        .alarmMin(0.0)
                        .alarmMax(0.0)
                        .build()
        );

        var measurement = MeasurementEntity.builder()
                .id(1L)
                .sensorEntity(sensorList.get(0))
                .timestamp(Instant.now())
                .unit(MeasurementEntity.Unit.CELSIUS)
                .value(25.0)
                .build()
        ;

        //when
        when(sensorRepository.findByUserEntityId(anyLong())).thenReturn(sensorList);

        when(measurementRepository.findTopBySensorEntityOrderByTimestampDesc(any(SensorEntity.class))).thenReturn(measurement);

        var result = sut.getUserMeasurements("1");

        //then

        assertNotNull(result);
        assertEquals(1, result.size());
        var dto = result.get(0);
        assertEquals(uuid1.toString(), dto.getUuid());
        assertEquals(measurement.getId(), dto.getMeasurementId());
        assertEquals("CELSIUS", dto.getUnit());
        assertEquals(measurement.getValue(), dto.getValue());
    }
}