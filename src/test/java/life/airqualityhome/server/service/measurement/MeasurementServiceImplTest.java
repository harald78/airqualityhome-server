package life.airqualityhome.server.service.measurement;


import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorBaseEntity;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceImplTest {
    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private MeasurementRepository measurementRepository;
    private MeasurementServiceImpl sut;


    @BeforeEach
    void setUp() {
        this.sut = new MeasurementServiceImpl(sensorRepository, measurementRepository);
    }

    @Test
    void getUserMeasurements() {

        //given
        UUID uuid1 = UUID.nameUUIDFromBytes("F0F0F0".getBytes());

        SensorBaseEntity sensorBase = SensorBaseEntity.builder().name("AZEnvy").build();
        SensorTypeEntity sensorType = SensorTypeEntity.builder().type(SensorTypeEntity.Type.TEMPERATURE).name("SHT30").build();

        SensorBaseSensorTypeEntity sensorBaseSensorType = new SensorBaseSensorTypeEntity();
        sensorBaseSensorType.setSensorType(sensorType);
        sensorBaseSensorType.setSensorBase(sensorBase);
        var sensorEntity = SensorEntity.builder()
                                           .id(1L)
                                           .uuid(uuid1)
                                           .sensorBaseSensorType(sensorBaseSensorType)
                                           .location("Living room")
                                           .alarmMin(0.0)
                                           .alarmMax(0.0)
                                           .build();

        var sensorList = List.of(sensorEntity);

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

        when(measurementRepository.findTopBySensorEntityOrderByTimestampDesc(any(SensorEntity.class))).thenReturn(Optional.of(measurement));

        var result = sut.getUserMeasurements("1");

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        var dto = result.get(0);
        assertEquals(uuid1.toString(), dto.getUuid());
        assertEquals(measurement.getId(), dto.getMeasurementId());
        assertEquals("CELSIUS", dto.getUnit());
        assertEquals(measurement.getValue(), dto.getValue());
        assertEquals(sensorEntity.getLocation(), dto.getLocation());
        assertEquals(sensorEntity.getLocation(), dto.getLocation());
        assertEquals(sensorEntity.getSensorBaseSensorType().getSensorType().getName(), dto.getSensorName());
        assertEquals(sensorEntity.getSensorBaseSensorType().getSensorType().getType().name(), dto.getSensorType());
        assertEquals(sensorEntity.getSensorBaseSensorType().getSensorBase().getName(), dto.getSensorBaseName());
        assertEquals(sensorEntity.getAlarmMin(), dto.getAlarmMin());
        assertEquals(sensorEntity.getAlarmMax(), dto.getAlarmMax());
        assertEquals(measurement.getTimestamp(), dto.getTimestamp());
        assertEquals(measurement.getUnit().name(), dto.getUnit());
    }
}