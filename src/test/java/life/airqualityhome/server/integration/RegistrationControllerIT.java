package life.airqualityhome.server.integration;

import life.airqualityhome.server.model.*;
import life.airqualityhome.server.repositories.*;
import life.airqualityhome.server.rest.controller.RegistrationController;
import life.airqualityhome.server.rest.dto.RegisterConfirmationDto;
import life.airqualityhome.server.rest.dto.RegisterRequestDto;
import life.airqualityhome.server.rest.dto.SensorBaseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RegistrationControllerIT {

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired DatabaseUtil databaseUtil;


    private RestTemplate restTemplate;


    @BeforeEach
    public void setup(){
        this.databaseUtil.setupSensorBaseAndSensorTypeDB();
        this.restTemplate = new RestTemplate();
    }

    @Test
    @DirtiesContext
    public void testGetSensorBasesFromDatabaseTest_HTTP_OK(){
        // when
        var result = restTemplate.getForEntity("http://localhost:8080/api/register/sensorBase", SensorBaseDto[].class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().length);
    }

    @Test
    @DirtiesContext
    public void testGetRegisterRequestForUserId1_HTTP_OK() {
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupRegisterRequestDB_forUserQuery();

        // when
        var result = restTemplate.getForEntity("http://localhost:8080/api/register/requests/" + 2L, RegisterRequestDto.class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Sleeping room", result.getBody().getLocation());
        assertEquals(2L, result.getBody().getUserId());
    }

    @Test
    @DirtiesContext
    public void testAddSensorBaseRegistration_HTTP_OK() {
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupRegisterRequestDB_forUserQuery();

        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(1L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Any location");

        // when
        var result = restTemplate.postForEntity("http://localhost:8080/api/register/sensor", registerRequestDto , RegisterRequestDto.class);

        assertNotNull(result);
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getId());
        assertTrue(result.getBody().getActive());
    }

    @Test
    @DirtiesContext
    public void testAddSensorBaseRegistration_HTTP_BAD_REQUEST() throws Exception{
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupRegisterRequestDB_forUserQuery();

        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(2L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Any location");

        // when
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity("http://localhost:8080/api/register/sensor", registerRequestDto , String.class));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A sensor registration is already in progress", exception.getResponseBodyAsString());
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

        var result = restTemplate.postForEntity("http://localhost:8080/api/register/sensor/confirm", registerConfirmation, String.class);

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

        var exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity("http://localhost:8080/api/register/sensor/confirm", registerConfirmation, String.class));

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

        var result = restTemplate.postForEntity("http://localhost:8080/api/register/sensor/confirm", registerConfirmation, String.class);
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

    @Test
    @DirtiesContext
    public void testCancelRegistration_HTTP_BAD_REQUEST_NoActiveRegistration() {
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupRegisterRequestDB_forUserQuery();

        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(1L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Any location");

        // when
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity("http://localhost:8080/api/register/sensor/cancel", registerRequestDto , String.class));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No sensor registration in progress for user 1", exception.getResponseBodyAsString());
    }

    @Test
    @DirtiesContext
    public void testCancelRegistration_HTTP_OK_requestCancelled() throws Exception {
        this.databaseUtil.setupUserForTest();
        this.databaseUtil.setupRegisterRequestDB_forUserQuery();

        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(2L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Sleeping room");

        // when
        var result = restTemplate.postForEntity("http://localhost:8080/api/register/sensor/cancel", registerRequestDto , RegisterRequestDto.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().getActive());
        assertTrue(result.getBody().getCanceled());
    }







}
