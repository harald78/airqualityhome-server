package life.airqualityhome.server.integration;

import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.repositories.SensorRepository;
import life.airqualityhome.server.rest.dto.RegisterConfirmationDto;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class SensorControllerIT {

    final static String BASE_URL = "http://localhost:8080/api/sensor";

    @Autowired
    DatabaseUtil databaseUtil;

    @Autowired
    SensorRepository sensorRepository;

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
    void shouldAddMeasurementAndAnswer_HTTP_OK() {
        // given
        String jsonPayload = "{"
                + "\"id\":\"F0F0F0\","
                + "\"base\":\"AZEnvy\","
                + "\"timestamp\":\"2024-06-04T22:20:46+0200\","
                + "\"measurements\":["
                + "{"
                + "\"value\":32.0,"
                + "\"unit\":\"CELSIUS\","
                + "\"type\":\"TEMPERATURE\""
                + "},"
                + "{"
                + "\"value\":2000.0,"
                + "\"unit\":\"PPM\","
                + "\"type\":\"GAS\""
                + "}"
                + "]"
                + "}";

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
        var result = restTemplate.postForEntity(BASE_URL + "/measurements", request, String.class);

        // then
        assertNotNull(result);
        assertEquals("OK", result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }


    @Test
    @DirtiesContext
    void shouldThrowIllegalStateExceptionAndReturn_HTTP_BAD_REQUEST() {
        // given
        String jsonPayload = "{"
                + "\"id\":\"F0F0F0\","
                + "\"base\":\"AZEnvy\","
                + "\"timestamp\":\"2024-06-04T22:20:46+0200\","
                + "\"measurements\":["
                + "{"
                + "\"value\":32.0,"
                + "\"unit\":\"CELSIUS\","
                + "\"type\":\"TEMPERATURE\""
                + "},"
                + "{"
                + "\"value\":55.0,"
                + "\"unit\":\"PERCENT\","
                + "\"type\":\"HUMIDITY\""
                + "}"
                + "]"
                + "}";

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        // when
        var exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity(BASE_URL + "/measurements", request, String.class));

        // then
        assertNotNull(exception);
        assertEquals("Sensor type HUMIDITY not found for base F0F0F0", exception.getResponseBodyAsString());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DirtiesContext
    public void testConfirmSensorRegistration_HTTP_OK_SensorAlreadyRegistered() {
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupSensorBaseSensorTypeRelations();
        this.databaseUtil.setupSensorsForTest();

        RegisterConfirmationDto registerConfirmation = new RegisterConfirmationDto();
        registerConfirmation.setUsername("user1");
        registerConfirmation.setUuid("F0F0F0");

        var result = restTemplate.postForEntity(BASE_URL + "/register/confirm", registerConfirmation, String.class);

        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Sensor with uuid F0F0F0 already registered", result.getBody());
    }

    @Test
    @DirtiesContext
    public void testConfirmSensorRegistration_HTTP_BAD_REQUEST_SensorAlreadyRegistered() throws Exception {
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupSensorBaseSensorTypeRelations();
        this.databaseUtil.setupSensorsForTest();

        RegisterConfirmationDto registerConfirmation = new RegisterConfirmationDto();
        registerConfirmation.setUsername("user1");
        registerConfirmation.setUuid("F0F0F3");

        var exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity(BASE_URL + "/register/confirm", registerConfirmation, String.class));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No sensor registration active for this user", exception.getResponseBodyAsString());
    }

    @Test
    @DirtiesContext
    public void testConfirmSensorRegistration_HTTP_OK_SensorsRegistered() throws Exception {
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupRegisterRequestDB_forUserQuery();
        this.databaseUtil.setupSensorBaseSensorTypeRelations();
        this.databaseUtil.setupSensorsForTest();

        UUID uuid = UUID.nameUUIDFromBytes("F0F0F3".getBytes());
        RegisterConfirmationDto registerConfirmation = new RegisterConfirmationDto();
        registerConfirmation.setUsername("user2");
        registerConfirmation.setUuid("F0F0F3");

        var result = restTemplate.postForEntity(BASE_URL + "/register/confirm", registerConfirmation, String.class);
        Optional<List<SensorEntity>> registeredSensors = sensorRepository.findByUuid(uuid);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("OK", result.getBody());
        assertTrue(registeredSensors.isPresent());
        assertEquals(2, registeredSensors.get().size());
        assertEquals(2L, registeredSensors.get().get(0).getUserId());
        assertEquals(2L, registeredSensors.get().get(1).getUserId());
        assertEquals("Sleeping room", registeredSensors.get().get(0).getLocation());
        assertEquals("Sleeping room", registeredSensors.get().get(1).getLocation());
    }

}
