package life.airqualityhome.server.integration;


import life.airqualityhome.server.AirqualityhomeServerApplication;
import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.repositories.SensorBaseRepository;
import life.airqualityhome.server.repositories.SensorTypeRepository;
import life.airqualityhome.server.rest.controller.RegistrationController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        AirqualityhomeServerApplication.class
})
@ActiveProfiles("test")
public class RegistrationControllerIT {

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    SensorBaseRepository sensorBaseRepository;

    @Autowired
    SensorTypeRepository sensorTypeRepository;

//    @Autowired
//    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        final var sensorType1 = SensorTypeEntity.builder()
                .id(1L)
                .type(SensorTypeEntity.Type.TEMPERATURE)
                .maxValue(-120.0)
                .minValue(-40.0)
                .name("SHT30")
                .build();
        final var sensorType2 = SensorTypeEntity.builder()
                .id(1L)
                .type(SensorTypeEntity.Type.GAS)
                .maxValue(100.0)
                .minValue(100000.0)
                .name("MQ-2")
                .build();
        sensorTypeRepository.saveAll(List.of(sensorType1, sensorType2));
        final var sensorBase = SensorBaseEntity.builder()
                .id(1L)
                .name("AZ-Envy")
                .sensorTypes(Set.of(
                        sensorType1,
                        sensorType2
                )).build();
        sensorBaseRepository.save(sensorBase);
    }

    @Test
    public void getSensorBasesFromDatabaseTest(){
        // given
        log.info("Test");


        // when

        // then
    }

}
