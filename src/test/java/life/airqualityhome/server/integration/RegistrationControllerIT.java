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

    final static String BASE_URL = "http://localhost:8080/api/app/register";


    @BeforeEach
    public void setup(){
        this.databaseUtil.setupSensorBaseAndSensorTypeDB();
        this.restTemplate = new RestTemplate();
    }

    @Test
    @DirtiesContext
    public void testGetSensorBasesFromDatabaseTest_HTTP_OK(){
        // when
        var result = restTemplate.getForEntity(BASE_URL + "/sensorBase", SensorBaseDto[].class);

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
        var result = restTemplate.getForEntity(BASE_URL + "/requests/" + 2L, RegisterRequestDto.class);

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
        var result = restTemplate.postForEntity(BASE_URL + "/sensor", registerRequestDto , RegisterRequestDto.class);

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
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity("http://localhost:8080/api/app/register/sensor", registerRequestDto , String.class));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A sensor registration is already in progress", exception.getResponseBodyAsString());
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
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity("http://localhost:8080/api/app/register/sensor/cancel", registerRequestDto , String.class));

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
        var result = restTemplate.postForEntity("http://localhost:8080/api/app/register/sensor/cancel", registerRequestDto , RegisterRequestDto.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().getActive());
        assertTrue(result.getBody().getCanceled());
    }







}
