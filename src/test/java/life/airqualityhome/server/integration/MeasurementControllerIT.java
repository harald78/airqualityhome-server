package life.airqualityhome.server.integration;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.SensorRawDataDto;
import life.airqualityhome.server.rest.dto.BaseRawDataDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class MeasurementControllerIT {

    final static String BASE_URL = "http://localhost:8080/api/app/measurements";

    @Autowired
    DatabaseUtil databaseUtil;

    private RestTemplate restTemplate;

    @BeforeEach
    public void setup(){
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupSensorBaseAndSensorTypeDB();
        this.databaseUtil.setupSensorBaseSensorTypeRelations();
        this.databaseUtil.setupSensorsForTest();
        this.restTemplate = new RestTemplate();
    }

    @Test
    @DirtiesContext
    void shouldReturnSensorMeasurements_HTTP_OK() {
        // given
        this.databaseUtil.setupMeasurementsForTest();
        var expectedTimestamp = LocalDateTime.of(2024, 6, 3, 12, 9, 0).toInstant(ZoneOffset.UTC);

        // when
        var result = restTemplate.getForEntity(BASE_URL + "/user/1", LatestMeasurementDto[].class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().length);
        assertEquals(MeasurementEntity.Unit.CELSIUS.name(), result.getBody()[0].getUnit());
        assertEquals(MeasurementEntity.Unit.PPM.name(), result.getBody()[1].getUnit());
        assertEquals(expectedTimestamp, result.getBody()[0].getTimestamp());
        assertEquals(expectedTimestamp, result.getBody()[1].getTimestamp());
    }

    @Test
    @DirtiesContext
    void shouldThrowErrorOnGetSensorMeasurements_BAD_REQUEST() {
        // given
        this.databaseUtil.setupMeasurementsForTest();

        // when
        var exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.getForEntity(BASE_URL + "/user/not-a-number", String.class));

        // then
        assertNotNull(exception);
        assertEquals("Invalid user id", exception.getResponseBodyAsString());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


}
