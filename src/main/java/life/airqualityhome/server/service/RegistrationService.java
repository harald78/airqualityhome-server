package life.airqualityhome.server.service;

import jakarta.transaction.Transactional;
import life.airqualityhome.server.model.RegisterRequestEntity;
import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.repositories.RegisterRequestRepository;
import life.airqualityhome.server.rest.dto.*;
import life.airqualityhome.server.rest.dto.mapper.RegisterRequestMapper;
import life.airqualityhome.server.rest.dto.mapper.SensorBaseMapper;
import life.airqualityhome.server.repositories.SensorBaseRepository;
import life.airqualityhome.server.rest.exceptions.NoSensorRegistrationActiveException;
import life.airqualityhome.server.rest.exceptions.RegistrationPendingException;
import life.airqualityhome.server.rest.exceptions.SensorRegistrationFailedException;
import life.airqualityhome.server.service.user.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class RegistrationService {


    private final SensorBaseRepository sensorBaseRepository;

    private final RegisterRequestRepository registerRequestRepository;

    private final SensorBaseMapper sensorBaseMapper;

    private final RegisterRequestMapper registerRequestMapper;

    private final UserService userService;

    private final SensorService sensorService;

    public RegistrationService(SensorBaseRepository sensorBaseRepository, RegisterRequestRepository registerRequestRepository,
                               SensorBaseMapper sensorBaseMapper, RegisterRequestMapper registerRequestMapper,
                               UserService userService, SensorService sensorService) {

        this.sensorBaseRepository = sensorBaseRepository;
        this.registerRequestRepository = registerRequestRepository;
        this.sensorBaseMapper = sensorBaseMapper;
        this.registerRequestMapper = registerRequestMapper;
        this.userService = userService;
        this.sensorService = sensorService;
    }

    public List<SensorBaseDto> getAvailableSensorBases() {
        var sensorBases = this.sensorBaseRepository.findAll();
        return StreamSupport.stream(sensorBases.spliterator(), false)
                .map(sensorBaseMapper::toDto)
                .toList();
    }

    public List<RegisterRequestDto> getRegisterRequestByUserId(Long userId) {
        Optional<List<RegisterRequestEntity>> entityList = registerRequestRepository.findAllByUserId(userId);
        return entityList.map(registerRequestEntities -> registerRequestEntities.stream().map(registerRequestMapper::toDto).toList()).orElseGet(ArrayList::new);
    }

    public RegisterRequestDto cancelRegisterRequest(RegisterRequestDto registerRequest) {
        Optional<RegisterRequestEntity> activeRegisterRequest = registerRequestRepository.findByUserIdAndActiveTrue(registerRequest.getUserId());
        if (activeRegisterRequest.isEmpty()) {
            log.error("No sensor registration in progress for user {}", registerRequest.getUserId());
            throw new NoSensorRegistrationActiveException("No sensor registration in progress for user " + registerRequest.getUserId());
        }
        RegisterRequestEntity registerRequestEntity = activeRegisterRequest.get();
        registerRequestEntity.setActive(false);
        registerRequestEntity.setCanceled(true);
        RegisterRequestEntity updatedEntity = registerRequestRepository.save(registerRequestEntity);
        return registerRequestMapper.toDto(updatedEntity);
    }

    public RegisterRequestDto registerSensorBase(RegisterRequestDto registerRequest) {
        Optional<RegisterRequestEntity> activeRegisterRequest = registerRequestRepository.findByUserIdAndActiveTrue(registerRequest.getUserId());
        if (activeRegisterRequest.isPresent()) {
            log.error("Sensor registration already in progress for user {}", registerRequest.getUserId());
            throw new RegistrationPendingException("A sensor registration is already in progress");
        }
        SensorBaseEntity sensorBase = sensorBaseRepository.findFirstById(registerRequest.getSensorBaseId());
        RegisterRequestEntity registerRequestEntity = RegisterRequestEntity.builder()
                .userId(registerRequest.getUserId())
                .sensorBase(sensorBase)
                .active(true)
                .canceled(false)
                .location(registerRequest.getLocation())
                .build();
        RegisterRequestEntity savedRequest = registerRequestRepository.save(registerRequestEntity);
        return registerRequestMapper.toDto(savedRequest);
    }

    @Transactional
    public String confirmSensorRegistration(RegisterConfirmationDto registerConfirmation) {
        UserResponseDto userResponseDto = userService.getUserByUserName(registerConfirmation.getUserName());
        Optional<RegisterRequestEntity> registerRequest = registerRequestRepository.findByUserIdAndActiveTrue(userResponseDto.getId());

        // Check if sensor is already registered
        List<SensorDto> registeredSensors = sensorService.getAllSensorsByUUID(registerConfirmation.getUuid());
        if (!registeredSensors.isEmpty()) {
            log.info("Sensor with uuid {} already registered", registerConfirmation.getUuid());
            return "Sensor with uuid " + registerConfirmation.getUuid() + " already registered";
        }
        // Check if registration request is present
        if (registerRequest.isEmpty()) {
            log.error("No sensor registration active for user {}", registerConfirmation.getUserName());
            throw new NoSensorRegistrationActiveException("No sensor registration active for this user");
        }

        // handle Sensor Request for Sensor UUID and user
        List<SensorDto> newSensors = sensorService.registerSensorsForUser(registerRequest.get(), userResponseDto.getId(), registerConfirmation.getUuid());
        if (newSensors.isEmpty()) {
            log.error("Sensor registration failed for user {} and sensor {}", registerConfirmation.getUserName(), registerConfirmation.getUuid());
            throw new SensorRegistrationFailedException("Sensor registration failed");
        }

        log.info("Sensor registration success for user {} and sensor {}", registerConfirmation.getUserName(), registerConfirmation.getUuid());
        return "OK";
    }



}
