package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.dto.RegisterConfirmationDto;
import life.airqualityhome.server.rest.dto.RegisterRequestDto;
import life.airqualityhome.server.rest.dto.SensorBaseDto;
import life.airqualityhome.server.rest.exceptions.NoSensorRegistrationActiveException;
import life.airqualityhome.server.rest.exceptions.RegistrationPendingException;
import life.airqualityhome.server.rest.exceptions.SensorRegistrationFailedException;
import life.airqualityhome.server.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PreAuthorize("hasAuthority('APP_WRITE')")
    @PostMapping("/sensor")
    public ResponseEntity<RegisterRequestDto> addSensorBaseRegistration(@RequestBody RegisterRequestDto registerRequest) {
        RegisterRequestDto registerRequestDto = registrationService.registerSensorBase(registerRequest);
        return new ResponseEntity<>(registerRequestDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('APP_WRITE')")
    @PostMapping("/sensor/cancel")
    public ResponseEntity<RegisterRequestDto> cancelSensorBaseRegistration(@RequestBody RegisterRequestDto registerRequest) {
        RegisterRequestDto registerRequestDto = registrationService.cancelRegisterRequest(registerRequest);
        return new ResponseEntity<>(registerRequestDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APP_READ')")
    @GetMapping("/requests/{id}")
    public ResponseEntity<RegisterRequestDto> getRegisterRequestByUserId(@PathVariable String id) {
        RegisterRequestDto sensorRegistrationsByUser = registrationService.getRegisterRequestByUserId(Long.parseLong(id));
        if (sensorRegistrationsByUser.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sensorRegistrationsByUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APP_READ')")
    @GetMapping("/sensorBase")
    public ResponseEntity<List<SensorBaseDto>> getAvailableSensorBases() {
        final var sensorBases = this.registrationService.getAvailableSensorBases();
        return new ResponseEntity<>(sensorBases, HttpStatus.OK);
    }

    @PostMapping("/sensor/confirm")
    public ResponseEntity<String> confirmSensorRegistration(@RequestBody RegisterConfirmationDto registerConfirmation) {
        var result = this.registrationService.confirmSensorRegistration(registerConfirmation);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RegistrationPendingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(NoSensorRegistrationActiveException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(SensorRegistrationFailedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
