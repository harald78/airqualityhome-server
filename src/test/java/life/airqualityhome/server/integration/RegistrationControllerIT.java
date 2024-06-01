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

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RegistrationControllerIT {

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    SensorBaseRepository sensorBaseRepository;

    @Autowired
    SensorTypeRepository sensorTypeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    SensorBaseSensorTypeRepository sensorBaseSensorTypeRepository;

    private RestTemplate restTemplate;
    @Autowired
    private RegisterRequestRepository registerRequestRepository;

    @BeforeEach
    public void setup(){
        this.setupSensorBaseAndSensorTypeDB();
        this.restTemplate = new RestTemplate();
    }

    @Test
    @DirtiesContext
    public void testGetSensorBasesFromDatabaseTest(){
        // when
        var result = restTemplate.getForEntity("http://localhost:8080/api/register/sensorBase", SensorBaseDto[].class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().length);
    }

    @Test
    @DirtiesContext
    public void testGetRegisterRequestForUserId1() {
        this.setupUserForTest();
        this.setupRegisterRequestDB_forUserQuery();

        // when
        var result = restTemplate.getForEntity("http://localhost:8080/api/register/requests/" + 2L, RegisterRequestDto.class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals("Sleeping room", result.getBody().getLocation());
        assertEquals(2L, result.getBody().getUserId());
    }

    @Test
    @DirtiesContext
    public void testAddSensorBaseRegistration_HTTP_OK() {
        this.setupUserForTest();
        this.setupRegisterRequestDB_forUserQuery();

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
        this.setupUserForTest();
        this.setupRegisterRequestDB_forUserQuery();

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
        this.setupUserForTest();
        this.setupSensorsForTest();

        RegisterConfirmationDto registerConfirmation = new RegisterConfirmationDto();
        registerConfirmation.setUserName("user1");
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
        this.setupUserForTest();
        this.setupSensorsForTest();

        RegisterConfirmationDto registerConfirmation = new RegisterConfirmationDto();
        registerConfirmation.setUserName("user1");
        registerConfirmation.setUuid("F0F0F3");

        var exception = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity("http://localhost:8080/api/register/sensor/confirm", registerConfirmation, String.class));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No sensor registration active for this user", exception.getResponseBodyAsString());
    }

    @Test
    @DirtiesContext
    public void testConfirmSensorRegistration_HTTP_OK_SensorsRegistered() throws Exception {
        this.setupUserForTest();
        this.setupRegisterRequestDB_forUserQuery();
        this.setupSensorsForTest();
        this.setupSensorBaseSensorTypeRelations();

        RegisterConfirmationDto registerConfirmation = new RegisterConfirmationDto();
        registerConfirmation.setUserName("user2");
        registerConfirmation.setUuid("F0F0F3");

        var result = restTemplate.postForEntity("http://localhost:8080/api/register/sensor/confirm", registerConfirmation, String.class);

        Optional<List<SensorEntity>> registeredSensors = sensorRepository.findByUuid("F0F0F3");

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
        this.setupUserForTest();
        this.setupRegisterRequestDB_forUserQuery();

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
        this.setupUserForTest();
        this.setupRegisterRequestDB_forUserQuery();

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


    private void setupSensorBaseAndSensorTypeDB() {
        SensorBaseEntity sensorBaseEntity = this.getSensorBaseEntityForTest();
        sensorBaseRepository.save(sensorBaseEntity);
    }

    private void setupSensorBaseSensorTypeRelations() {
        final var sensorBaseSensorType1 = SensorBaseSensorTypeEntity.builder()
                .sensorTypeId(1L)
                .sensorBaseId(1L)
                .build();
        final var sensorBaseSensorType2 = SensorBaseSensorTypeEntity.builder()
                .sensorTypeId(2L)
                .sensorBaseId(1L)
                .build();
        sensorBaseSensorTypeRepository.saveAll(List.of(sensorBaseSensorType1, sensorBaseSensorType2));
    }

    private void setupRegisterRequestDB_forUserQuery() {
        SensorBaseEntity sensorBaseEntity = this.getSensorBaseEntityForTest();
        RegisterRequestEntity entity1 = RegisterRequestEntity.builder()
                .userId(1L)
                .sensorBase(sensorBaseEntity)
                .location("Living room")
                .active(false)
                .build();
        RegisterRequestEntity entity2 = RegisterRequestEntity.builder()
                .userId(2L)
                .sensorBase(sensorBaseEntity)
                .location("Sleeping room")
                .active(true)
                .build();
        RegisterRequestEntity entity3 = RegisterRequestEntity.builder()
                .userId(2L)
                .sensorBase(sensorBaseEntity)
                .location("Bath room")
                .active(false)
                .build();
        registerRequestRepository.saveAll(List.of(entity1, entity2, entity3));
    }

    private void setupUserForTest() {
        UserRoleEntity roleRead = UserRoleEntity.builder().id(1L).name("APP_READ").build();
        UserRoleEntity roleWrite = UserRoleEntity.builder().id(1L).name("APP_WRITE").build();
        this.roleRepository.saveAll(List.of(roleRead, roleWrite));

        UserEntity user1 = UserEntity.builder().id(1L).password("pw1").email("test@test.de").username("user1").roles(Set.of(roleRead, roleWrite)).build();
        UserEntity user2 = UserEntity.builder().id(2L).password("pw2").email("test2@test.de").username("user2").roles(Set.of(roleRead, roleWrite)).build();
        this.userRepository.saveAll(List.of(user1, user2));
    }

    private void setupSensorsForTest(){
        SensorEntity sensorEntity1 = SensorEntity.builder()
                .sensorBaseSensorTypeId(1L)
                .userId(1L)
                .location("Living room")
                .alarmMin(0.0)
                .alarmMax(0.0)
                .alarmActive(false)
                .uuid("F0F0F0")
                .build();
        SensorEntity sensorEntity2 = SensorEntity.builder()
                .sensorBaseSensorTypeId(2L)
                .userId(1L)
                .location("Living room")
                .alarmMin(0.0)
                .alarmMax(0.0)
                .alarmActive(false)
                .uuid("F0F0F0")
                .build();
        SensorEntity sensorEntity3 = SensorEntity.builder()
                .sensorBaseSensorTypeId(1L)
                .userId(2L)
                .location("Bath room")
                .alarmMin(0.0)
                .alarmMax(0.0)
                .alarmActive(false)
                .uuid("F0F0F1")
                .build();
        SensorEntity sensorEntity4 = SensorEntity.builder()
                .sensorBaseSensorTypeId(2L)
                .userId(2L)
                .location("Bath room")
                .alarmMin(0.0)
                .alarmMax(0.0)
                .alarmActive(false)
                .uuid("F0F0F1")
                .build();
        sensorRepository.saveAll(List.of(sensorEntity1, sensorEntity2, sensorEntity3, sensorEntity4));
    }

    private SensorBaseEntity getSensorBaseEntityForTest() {
        final var sensorType1 = SensorTypeEntity.builder()
                .id(1L)
                .type(SensorTypeEntity.Type.TEMPERATURE)
                .maxValue(-120.0)
                .minValue(-40.0)
                .name("SHT30")
                .build();
        final var sensorType2 = SensorTypeEntity.builder()
                .id(2L)
                .type(SensorTypeEntity.Type.GAS)
                .maxValue(100.0)
                .minValue(100000.0)
                .name("MQ-2")
                .build();
        sensorTypeRepository.saveAll(List.of(sensorType1, sensorType2));
        return SensorBaseEntity.builder()
                .id(1L)
                .name("AZ-Envy")
                .sensorTypes(Set.of(
                        sensorType1,
                        sensorType2
                )).build();
    }

}
