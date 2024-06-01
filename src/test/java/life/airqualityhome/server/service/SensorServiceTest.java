package life.airqualityhome.server.service;

import life.airqualityhome.server.model.*;
import life.airqualityhome.server.repositories.SensorBaseSensorTypeRepository;
import life.airqualityhome.server.repositories.SensorRepository;
import life.airqualityhome.server.rest.dto.SensorDto;
import life.airqualityhome.server.rest.dto.mapper.SensorMapper;
import life.airqualityhome.server.rest.dto.mapper.SensorMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.UUID;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class SensorServiceTest {

    @Mock
    SensorBaseSensorTypeRepository sensorBaseSensorTypeRepository;

    @Mock
    SensorRepository sensorRepository;

    SensorMapper sensorMapper;

    SensorService sut;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.sensorMapper = new SensorMapperImpl();
        this.sut = new SensorService(sensorBaseSensorTypeRepository, sensorRepository, sensorMapper);
    }

    @Test
    void getAllSensorsByUUID_shouldReturnEmptyList() {
        // given
        String uuid = "F0F0F0";

        // when
        when(sensorRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());
        var result = sut.getAllSensorsByUUID(uuid);

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getAllSensorsByUUID_shouldSensorList() {
        // given
        String uuid = "F0F0F0";
        List<SensorEntity> sensorEntities = List.of(
                SensorEntity.builder().id(1L).sensorBaseSensorTypeId(1L).userId(1L).location("Living room").alarmMax(0.0).alarmMin(0.0).alarmActive(false).build(),
                SensorEntity.builder().id(2L).sensorBaseSensorTypeId(2L).userId(1L).location("Living room").alarmMax(0.0).alarmMin(0.0).alarmActive(false).build()
        );

        // when
        when(sensorRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(sensorEntities));
        var result = sut.getAllSensorsByUUID(uuid);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(SensorDto.class, result.get(0).getClass());
    }

    @Test
    void registerSensorForUser() {
        // given
        SensorTypeEntity sensorType1 = SensorTypeEntity.builder()
                .id(1L)
                .type(SensorTypeEntity.Type.TEMPERATURE)
                .maxValue(-120.0)
                .minValue(-40.0)
                .name("SHT30")
                .build();
        SensorTypeEntity sensorType2 = SensorTypeEntity.builder()
                .id(1L)
                .type(SensorTypeEntity.Type.GAS)
                .maxValue(100.0)
                .minValue(100000.0)
                .name("MQ-2")
                .build();
        SensorBaseEntity sensorBase = SensorBaseEntity.builder()
                .id(1L)
                .name("AZ-Envy")
                .sensorTypes(Set.of(sensorType1, sensorType2)).build();
        RegisterRequestEntity registerRequestEntity = RegisterRequestEntity.builder()
                .userId(1L).sensorBase(sensorBase).location("Living room").active(true).build();
        List<SensorBaseSensorTypeEntity> sensorBaseSensorTypeRelations = List.of(
                SensorBaseSensorTypeEntity.builder().id(1L).sensorBaseEntityId(sensorBase.getId()).sensorTypesId(sensorType1.getId()).build(),
                SensorBaseSensorTypeEntity.builder().id(2L).sensorBaseEntityId(sensorBase.getId()).sensorTypesId(sensorType2.getId()).build());

        // when
        when(sensorBaseSensorTypeRepository.findAllBySensorBaseIdAndSensorTypeIdIn(anyLong(), anyList())).thenReturn(sensorBaseSensorTypeRelations);
        when(sensorRepository.saveAll(anyIterable())).thenAnswer(new Answer<Iterable<SensorEntity>>() {
            @Override
            public Iterable<SensorEntity> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                return (Iterable<SensorEntity>) args[0];
            }
        });
        var result = sut.registerSensorsForUser(registerRequestEntity, 1L, "F0F0F0");

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(SensorDto.class, result.get(0).getClass());
        verify(sensorRepository, times(1)).saveAll(anyIterable());
    }


}
