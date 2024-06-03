package life.airqualityhome.server.service;

import life.airqualityhome.server.model.RegisterRequestEntity;
import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.repositories.RegisterRequestRepository;
import life.airqualityhome.server.rest.dto.*;
import life.airqualityhome.server.rest.dto.mapper.RegisterRequestMapper;
import life.airqualityhome.server.rest.dto.mapper.RegisterRequestMapperImpl;
import life.airqualityhome.server.rest.dto.mapper.SensorBaseMapper;
import life.airqualityhome.server.repositories.SensorBaseRepository;
import life.airqualityhome.server.rest.dto.mapper.SensorBaseMapperImpl;
import life.airqualityhome.server.rest.exceptions.NoSensorRegistrationActiveException;
import life.airqualityhome.server.rest.exceptions.RegistrationPendingException;
import life.airqualityhome.server.rest.exceptions.SensorRegistrationFailedException;
import life.airqualityhome.server.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class RegistrationServiceTest {

    @Mock
    private SensorBaseRepository sensorBaseRepository;

    @Mock
    private RegisterRequestRepository registerRequestRepository;

    private RegistrationService sut;

    private SensorBaseMapper mapper;

    private RegisterRequestMapper registerRequestMapper;

    @Mock
    private UserService userService;

    @Mock
    private SensorService sensorService;


    @BeforeEach
    public void setup() {
        openMocks(this);
        mapper = new SensorBaseMapperImpl();
        registerRequestMapper = new RegisterRequestMapperImpl();
        sut = new RegistrationService(sensorBaseRepository, registerRequestRepository, mapper, registerRequestMapper, userService, sensorService);
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

    @Test
    void registerSensor_shouldRegisterSensor() {
        // given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(1L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Living room");
        SensorBaseEntity sensorBase = SensorBaseEntity.builder()
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
                )).build();

        // when
        when(registerRequestRepository.findByUserIdAndActiveTrue(1L)).thenReturn(Optional.empty());
        when(sensorBaseRepository.findFirstById(1L)).thenReturn(sensorBase);
        when(registerRequestRepository.save(any(RegisterRequestEntity.class))).thenAnswer(new Answer<RegisterRequestEntity>() {
            @Override
            public RegisterRequestEntity answer (InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                RegisterRequestEntity resp = (RegisterRequestEntity) args[0];
                resp.setLocation("Living room");
                resp.setId(1L);
                resp.setActive(true);
                return (RegisterRequestEntity) args[0];
            }
        });
        var result = sut.registerSensorBase(registerRequestDto);

        // then
        assertNotNull(result);
        assertEquals(RegisterRequestDto.class, result.getClass());
        assertEquals(1, result.getId());
        assertTrue(result.getActive());
        assertEquals(1L , result.getUserId());
        assertEquals(1L, result.getSensorBaseId());
        assertEquals("Living room", result.getLocation());
        verify(registerRequestRepository, times(1)).findByUserIdAndActiveTrue(anyLong());
        verify(sensorBaseRepository, times(1)).findFirstById(anyLong());
        verify(registerRequestRepository, times(1)).save(any(RegisterRequestEntity.class));
    }

    @Test
    void registerSensor_shouldThrowException_whenRequestIsPending() {
        // given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(1L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Living room");
        SensorBaseEntity sensorBase = SensorBaseEntity.builder()
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
                )).build();

        // when
        when(registerRequestRepository.findByUserIdAndActiveTrue(1L)).thenReturn(Optional.of(RegisterRequestEntity.builder().id(1L).userId(1L).sensorBase(sensorBase).active(true).build()));
        when(sensorBaseRepository.findFirstById(1L)).thenReturn(sensorBase);
        when(registerRequestRepository.save(any(RegisterRequestEntity.class))).thenAnswer(new Answer<RegisterRequestEntity>() {
            @Override
            public RegisterRequestEntity answer (InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                RegisterRequestEntity resp = (RegisterRequestEntity) args[0];
                resp.setId(1L);
                resp.setActive(true);
                return (RegisterRequestEntity) args[0];
            }
        });
        assertThrows(RegistrationPendingException.class, () -> sut.registerSensorBase(registerRequestDto));

        // then
        verify(registerRequestRepository, times(1)).findByUserIdAndActiveTrue(anyLong());
        verify(sensorBaseRepository, times(0)).findFirstById(anyLong());
        verify(registerRequestRepository, times(0)).save(any(RegisterRequestEntity.class));
    }

    @Test
    void registerSensor_shouldThrowException_whenNoRequestIsActive() {
        // given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(1L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Living room");

        // when
        when(registerRequestRepository.findByUserIdAndActiveTrue(1L)).thenReturn(Optional.empty());
        assertThrows(NoSensorRegistrationActiveException.class, () -> sut.cancelRegisterRequest(registerRequestDto));

        // then
        verify(registerRequestRepository, times(1)).findByUserIdAndActiveTrue(anyLong());
        verify(sensorBaseRepository, times(0)).findFirstById(anyLong());
        verify(registerRequestRepository, times(0)).save(any(RegisterRequestEntity.class));
    }

    @Test
    void registerSensor_shouldCancelRegistration() {
        // given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserId(1L);
        registerRequestDto.setSensorBaseId(1L);
        registerRequestDto.setLocation("Living room");
        SensorBaseEntity sensorBase = SensorBaseEntity.builder()
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
                )).build();

        // when
        when(registerRequestRepository.findByUserIdAndActiveTrue(1L)).thenReturn(Optional.of(RegisterRequestEntity.builder().id(1L).userId(1L).sensorBase(sensorBase).active(true).build()));
        when(registerRequestRepository.save(any(RegisterRequestEntity.class))).thenAnswer(new Answer<RegisterRequestEntity>() {
            @Override
            public RegisterRequestEntity answer (InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                RegisterRequestEntity resp = (RegisterRequestEntity) args[0];
                resp.setId(1L);
                return (RegisterRequestEntity) args[0];
            }
        });
        var result = sut.cancelRegisterRequest(registerRequestDto);

        // then
        assertNotNull(result);
        assertEquals(RegisterRequestDto.class, result.getClass());
        assertFalse(result.getActive());
        assertTrue(result.getCanceled());
        verify(registerRequestRepository, times(1)).findByUserIdAndActiveTrue(anyLong());
        verify(registerRequestRepository, times(1)).save(any(RegisterRequestEntity.class));
    }

    @Test
    void getRegisterSensorRequestForUser() {
        // given
        Long userId = 1L;

        // when
        when(registerRequestRepository.findAllByUserId(1L)).thenReturn(Optional.of(List.of(
                RegisterRequestEntity.builder().userEntity(UserEntity.builder().build()).userId(1L).active(true).sensorBase(SensorBaseEntity.builder().build()).build(),
                RegisterRequestEntity.builder().userEntity(UserEntity.builder().build()).userId(1L).active(false).sensorBase(SensorBaseEntity.builder().build()).build()
        )));
        var result = sut.getRegisterRequestByUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(RegisterRequestDto.class, result.getClass());
        verify(registerRequestRepository, times(1)).findByUserIdAndActiveTrue(1L);
    }

    @Test
    void getRegisterSensorRequestForUser_shouldReturnEmptyList() {
        // given
        Long userId = 1L;

        // when
        when(registerRequestRepository.findAllByUserId(1L)).thenReturn(Optional.empty());
        var result = sut.getRegisterRequestByUserId(userId);

        // then
        assertNotNull(result);
        assertNull(result.getId());
        verify(registerRequestRepository, times(1)).findByUserIdAndActiveTrue(1L);
    }

    @Test
    void confirmSensorRegistration_shouldReturnOK_sensorAlreadyRegistered() {
        // given
        RegisterConfirmationDto registerConfirmationDto = new RegisterConfirmationDto();
        registerConfirmationDto.setUsername("any-username");
        registerConfirmationDto.setSensorId("F0F0F0");
        List<SensorDto> sensorList = List.of(
                SensorDto.builder().sensorBaseSensorTypeId(1L).uuid("F0F0F0").id(1L).location("Living room").alarmMin(0.0).alarmMax(0.0).alarmActive(false).build(),
                SensorDto.builder().sensorBaseSensorTypeId(2L).uuid("F0F0F0").id(1L).location("Living room").alarmMin(0.0).alarmMax(0.0).alarmActive(false).build()
        );

        // when
        when(userService.getUserByUserName(anyString()))
                .thenReturn(UserResponseDto.builder()
                        .id(1L).username("any-username").roles(Set.of(UserRoleDto.builder().name("APP_READ").id(1L)
                                .build())).build());
        when(sensorService.getAllSensorsByUUID("F0F0F0")).thenReturn(sensorList);
        var result = sut.confirmSensorRegistration(registerConfirmationDto);

        // then
        assertNotNull(result);
        assertEquals("Sensor with uuid F0F0F0 already registered", result);
        verify(sensorService, times(0)).registerSensorsForUser(any(RegisterRequestEntity.class), anyLong(), anyString());
    }

    @Test
    void confirmSensorRegistration_shouldThrow_noRegistrationActive() {
        // given
        RegisterConfirmationDto registerConfirmationDto = new RegisterConfirmationDto();
        registerConfirmationDto.setUsername("any-username");
        registerConfirmationDto.setSensorId("F0F0F0");
        List<SensorDto> sensorList = List.of();

        // when
        when(userService.getUserByUserName(anyString()))
                .thenReturn(UserResponseDto.builder()
                        .id(1L).username("any-username").roles(Set.of(UserRoleDto.builder().name("APP_READ").id(1L)
                                .build())).build());
        when(sensorService.getAllSensorsByUUID("F0F0F0")).thenReturn(sensorList);
        when(registerRequestRepository.findByUserIdAndActiveTrue(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSensorRegistrationActiveException.class, () -> sut.confirmSensorRegistration(registerConfirmationDto));

        // then
        verify(sensorService, times(0)).registerSensorsForUser(any(RegisterRequestEntity.class), anyLong(), anyString());
    }

    @Test
    void confirmSensorRegistration_returnOK_sensorsRegisteredSuccessfully() {
        // given
        RegisterConfirmationDto registerConfirmationDto = new RegisterConfirmationDto();
        registerConfirmationDto.setUsername("any-username");
        registerConfirmationDto.setSensorId("F0F0F0");
        List<SensorDto> sensorList = List.of();
        RegisterRequestEntity registerRequestEntity = RegisterRequestEntity.builder()
                .userId(1L).sensorBase(SensorBaseEntity.builder()
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
                        )).build()).location("Living room").active(true).build();
        List<SensorDto> sensorDtoList = List.of(
                SensorDto.builder()
                        .id(1L)
                        .sensorBaseSensorTypeId(1L)
                        .uuid("F0F0F0")
                        .location("Living room")
                        .alarmMax(0.0)
                        .alarmMin(0.0)
                        .alarmActive(false)
                        .build(),
                SensorDto.builder()
                        .id(2L)
                        .sensorBaseSensorTypeId(2L)
                        .uuid("F0F0F0")
                        .location("Living room")
                        .alarmMax(0.0)
                        .alarmMin(0.0)
                        .alarmActive(false)
                        .build());

        // when
        when(userService.getUserByUserName(anyString()))
                .thenReturn(UserResponseDto.builder()
                        .id(1L).username("any-username").roles(Set.of(UserRoleDto.builder().name("APP_READ").id(1L)
                                .build())).build());
        when(sensorService.getAllSensorsByUUID("F0F0F0")).thenReturn(sensorList);
        when(registerRequestRepository.findByUserIdAndActiveTrue(anyLong())).thenReturn(Optional.of(registerRequestEntity));
        when(sensorService.registerSensorsForUser(any(RegisterRequestEntity.class), anyLong(), anyString())).thenReturn(sensorDtoList);
        when(registerRequestRepository.save(any(RegisterRequestEntity.class))).thenAnswer(new Answer<RegisterRequestEntity>() {
            @Override
            public RegisterRequestEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
                var entity = (RegisterRequestEntity) invocationOnMock.getArgument(0);
                assertFalse(entity.getActive());
                return entity;
            }
        });

        var result = sut.confirmSensorRegistration(registerConfirmationDto);

        // then
        assertNotNull(result);
        assertEquals("OK", result);
        verify(sensorService, times(1)).registerSensorsForUser(any(RegisterRequestEntity.class), anyLong(), anyString());
        verify(registerRequestRepository, times(1)).save(registerRequestEntity);
    }

    @Test
    void confirmSensorRegistration_throwFailedError_sensorListEmpty() {
        // given
        RegisterConfirmationDto registerConfirmationDto = new RegisterConfirmationDto();
        registerConfirmationDto.setUsername("any-username");
        registerConfirmationDto.setSensorId("F0F0F0");
        List<SensorDto> sensorList = List.of();
        RegisterRequestEntity registerRequestEntity = RegisterRequestEntity.builder()
                .userId(1L).sensorBase(SensorBaseEntity.builder()
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
                        )).build()).location("Living room").active(true).build();
        List<SensorDto> sensorDtoList = List.of();

        // when
        when(userService.getUserByUserName(anyString()))
                .thenReturn(UserResponseDto.builder()
                        .id(1L).username("any-username").roles(Set.of(UserRoleDto.builder().name("APP_READ").id(1L)
                                .build())).build());
        when(sensorService.getAllSensorsByUUID("F0F0F0")).thenReturn(sensorList);
        when(registerRequestRepository.findByUserIdAndActiveTrue(anyLong())).thenReturn(Optional.of(registerRequestEntity));
        when(sensorService.registerSensorsForUser(registerRequestEntity, 1L, "any-username")).thenReturn(sensorDtoList);

        assertThrows(SensorRegistrationFailedException.class, () -> sut.confirmSensorRegistration(registerConfirmationDto));

        // then
        verify(sensorService, times(1)).registerSensorsForUser(any(RegisterRequestEntity.class), anyLong(), anyString());
    }


}