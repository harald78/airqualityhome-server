package life.airqualityhome.server.service;

import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.rest.dto.mapper.SensorBaseMapper;
import life.airqualityhome.server.repositories.SensorBaseRepository;
import life.airqualityhome.server.rest.dto.SensorBaseDto;
import life.airqualityhome.server.rest.dto.mapper.SensorBaseMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class RegistrationServiceTest {

    @Mock
    private SensorBaseRepository sensorBaseRepository;

    private RegistrationService sut;

    private SensorBaseMapper mapper;

    @BeforeEach
    public void setup() {
        openMocks(this);
        mapper = new SensorBaseMapperImpl();
        sut = new RegistrationService(sensorBaseRepository, mapper);
    }

    @Test
    void getAvailableSensorBases() {
        // given
        Iterable<SensorBaseEntity> sensorBaseEntities = List.of(
                SensorBaseEntity.builder()
                        .id(1L)
                        .name("AZ-Envy")
                        .sensorTypes(Set.of(
                                SensorTypeEntity.builder()
                                        .id(1L)
                                        .type(SensorTypeEntity.Type.TEMPERATURE)
                                        .maxValue(-120.0)
                                        .minValue(-40.0)
                                        .name("SHT30")
                                        .build(),
                                SensorTypeEntity.builder()
                                        .id(1L)
                                        .type(SensorTypeEntity.Type.GAS)
                                        .maxValue(100.0)
                                        .minValue(100000.0)
                                        .name("MQ-2")
                                        .build()
                                )).build());


        // when
        when(sensorBaseRepository.findAll()).thenReturn(sensorBaseEntities);
        var result = sut.getAvailableSensorBases();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SensorBaseDto.class, result.get(0).getClass());
        assertEquals(2, result.get(0).getSensorTypes().size());
        verify(sensorBaseRepository, times(1)).findAll();
    }
}