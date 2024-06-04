package life.airqualityhome.server.service.measurement;


import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.model.SensorBaseSensorTypeEntity;
import life.airqualityhome.server.model.SensorEntity;

import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.repositories.MeasurementRepository;
import life.airqualityhome.server.rest.dto.SensorRawDataDto;
import life.airqualityhome.server.rest.dto.mapper.BaseRawDataDto;
import life.airqualityhome.server.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceImplTest {
    @Mock
    private SensorService sensorService;

    @Mock
    private MeasurementRepository measurementRepository;
    private MeasurementServiceImpl sut;


    @BeforeEach
    void setUp() {
        this.sut = new MeasurementServiceImpl(sensorService, measurementRepository);
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
                .sensorValue(25.0)
                .build()
        ;

        //when
        when(sensorService.getSensorEntitiesForUser(anyLong())).thenReturn(sensorList);
        when(measurementRepository.findTopBySensorEntityOrderByTimestampDesc(any(SensorEntity.class))).thenReturn(Optional.of(measurement));

        var result = sut.getUserMeasurements("1");

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        var dto = result.get(0);
        assertEquals(uuid1.toString(), dto.getUuid());
        assertEquals(measurement.getId(), dto.getMeasurementId());
        assertEquals("CELSIUS", dto.getUnit());
        assertEquals(measurement.getSensorValue(), dto.getValue());
        assertEquals(sensorEntity.getLocation(), dto.getLocation());
        assertEquals(sensorEntity.getLocation(), dto.getLocation());
        assertEquals(sensorEntity.getSensorBaseSensorType().getSensorType().getName(), dto.getSensorName());
        assertEquals(sensorEntity.getSensorBaseSensorType().getSensorType().getType().name(), dto.getSensorType());
        assertEquals(sensorEntity.getSensorBaseSensorType().getSensorBase().getName(), dto.getSensorBaseName());
        assertEquals(sensorEntity.getAlarmMin(), dto.getAlarmMin());
        assertEquals(sensorEntity.getAlarmMax(), dto.getAlarmMax());
        assertEquals(measurement.getTimestamp(), dto.getTimestamp());
        assertEquals(measurement.getUnit().name(), dto.getUnit());
        verify(sensorService, times(1)).getSensorEntitiesForUser(anyLong());
        verify(measurementRepository, times(1)).findTopBySensorEntityOrderByTimestampDesc(any(SensorEntity.class));
    }

    @Test
    void shouldFindSensorTypeInSensorList() {
        // given
        List<SensorEntity> sensorEntities = this.getSensorEntitiesForTest();

        // when
        var result = sut.filterBySensorType(sensorEntities, SensorTypeEntity.Type.HUMIDITY);

        // then
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
    }

    @Test
    void shouldGetMeasurementEntity() {
        // given
        List<SensorEntity> sensorEntities = this.getSensorEntitiesForTest();
        LocalDateTime timestamp = LocalDateTime.of(2024, 6, 3, 22, 50, 0);

        // when
        var result = sut.getMeasurementEntity(getRawMeasurementsForTest().get(0), sensorEntities, timestamp, "F0F0F0");

        // then
        assertNotNull(result);
        assertEquals(MeasurementEntity.class, result.getClass());
        assertEquals(MeasurementEntity.Unit.CELSIUS, result.getUnit());
        assertEquals(SensorTypeEntity.Type.TEMPERATURE, result.getSensorEntity().getSensorBaseSensorType().getSensorType().getType());
        assertEquals(32.0, result.getSensorValue());
    }

    @Test
    void shouldThrowIllegalStateException() {
        // given
        List<SensorEntity> sensorEntities = this.getSensorEntitiesForTest();
        LocalDateTime timestamp = LocalDateTime.of(2024, 6, 3, 22, 50, 0);
        SensorRawDataDto rawDataDto = SensorRawDataDto.builder()
                                                      .type(SensorTypeEntity.Type.GAS)
                                                      .unit(MeasurementEntity.Unit.PPM)
                                                      .value(3000.0).build();

        // when
        var result = assertThrows(IllegalStateException.class, () -> sut.getMeasurementEntity(rawDataDto, sensorEntities, timestamp, "F0F0F0"));

        assertNotNull(result);
        assertEquals("Sensor type GAS not found for base F0F0F0", result.getMessage());
    }

    @Test
    void shouldAddMeasurementsAndSaveData() {
        // given
        LocalDateTime timestamp = LocalDateTime.of(2024, 6, 3, 22, 50, 0);
        BaseRawDataDto baseRawDataDto = BaseRawDataDto.builder()
            .id("F0F0F0")
            .base("AZEnvy")
            .timestamp(timestamp)
            .measurements(getRawMeasurementsForTest()).build();

        // when
        when(sensorService.getAllSensorEntitiesByUUID(anyString())).thenReturn(getSensorEntitiesForTest());
        when(measurementRepository.saveAll(anyList())).thenAnswer(new Answer<List<MeasurementEntity>>() {
            @Override
            public List<MeasurementEntity> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object argument = invocationOnMock.getArguments()[0];
                List<MeasurementEntity> measurements = new ArrayList<>();
                for (Object o : (List<?>) argument) {
                    if (!(o instanceof MeasurementEntity)) {
                        throw new ClassCastException();
                    } else {
                        measurements.add((MeasurementEntity) o);
                    }
                }
                assertEquals(2, measurements.size());
                assertEquals(SensorTypeEntity.Type.TEMPERATURE, measurements.get(0).getSensorEntity().getSensorBaseSensorType().getSensorType().getType());
                assertEquals(MeasurementEntity.Unit.CELSIUS, measurements.get(0).getUnit());
                assertEquals(32.0, measurements.get(0).getSensorValue());
                assertEquals(SensorTypeEntity.Type.HUMIDITY, measurements.get(1).getSensorEntity().getSensorBaseSensorType().getSensorType().getType());
                assertEquals(MeasurementEntity.Unit.PERCENT, measurements.get(1).getUnit());
                assertEquals(55.0, measurements.get(1).getSensorValue());
                return measurements;
            }
        });
        var result = sut.addMeasurements(baseRawDataDto);

        assertTrue(result);
        verify(measurementRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldReturnFalseOnSensorEntitiesEmpty() {
        // given
        LocalDateTime timestamp = LocalDateTime.of(2024, 6, 3, 22, 50, 0);
        BaseRawDataDto baseRawDataDto = BaseRawDataDto.builder()
                                                      .id("F0F0F0")
                                                      .base("AZEnvy")
                                                      .timestamp(timestamp)
                                                      .measurements(getRawMeasurementsForTest()).build();

        // when
        when(sensorService.getAllSensorEntitiesByUUID(anyString())).thenReturn(List.of());
        var result = sut.addMeasurements(baseRawDataDto);

        assertFalse(result);
        verify(measurementRepository, times(0)).saveAll(anyList());
    }

    List<SensorRawDataDto> getRawMeasurementsForTest() {
        return List.of(SensorRawDataDto.builder()
                                       .type(SensorTypeEntity.Type.TEMPERATURE)
                                       .unit(MeasurementEntity.Unit.CELSIUS)
                                       .value(32.0).build(),
                       SensorRawDataDto.builder()
                                       .type(SensorTypeEntity.Type.HUMIDITY)
                                       .unit(MeasurementEntity.Unit.PERCENT)
                                       .value(55.0).build()); }

    List<SensorEntity> getSensorEntitiesForTest() {
        SensorBaseSensorTypeEntity sensorBaseSensorType = new SensorBaseSensorTypeEntity();
        sensorBaseSensorType.setId(1L);
        sensorBaseSensorType.setSensorTypesId(1L);
        sensorBaseSensorType.setSensorBaseEntityId(1L);
        sensorBaseSensorType.setSensorBase(SensorBaseEntity.builder().id(1L).build());
        sensorBaseSensorType.setSensorType(SensorTypeEntity.builder().id(1L).type(SensorTypeEntity.Type.TEMPERATURE).build());
        SensorBaseSensorTypeEntity sensorBaseSensorType2 = new SensorBaseSensorTypeEntity();
        sensorBaseSensorType2.setId(2L);
        sensorBaseSensorType2.setSensorTypesId(2L);
        sensorBaseSensorType2.setSensorBaseEntityId(1L);
        sensorBaseSensorType2.setSensorBase(SensorBaseEntity.builder().id(1L).build());
        sensorBaseSensorType2.setSensorType(SensorTypeEntity.builder().id(2L).type(SensorTypeEntity.Type.HUMIDITY).build());

        return List.of(
            SensorEntity.builder().id(1L).sensorBaseSensorTypeId(1L).sensorBaseSensorType(sensorBaseSensorType).build(),
            SensorEntity.builder().id(2L).sensorBaseSensorTypeId(2L).sensorBaseSensorType(sensorBaseSensorType2).build());
    }


}