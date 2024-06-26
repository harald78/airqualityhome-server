package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.dto.BaseRawDataDto;
import life.airqualityhome.server.rest.dto.RegisterConfirmationDto;
import life.airqualityhome.server.rest.dto.SensorDto;
import life.airqualityhome.server.rest.exceptions.NoSensorRegistrationActiveException;
import life.airqualityhome.server.service.RegistrationService;
import life.airqualityhome.server.service.SensorService;
import life.airqualityhome.server.service.measurement.MeasurementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor")
public class SensorController {

    private final RegistrationService registrationService;

    private final MeasurementService measurementService;

    public SensorController(RegistrationService registrationService, MeasurementService measurementService) {
        this.registrationService = registrationService;
        this.measurementService = measurementService;
    }

    @PostMapping(value = "/register/confirm", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> confirmSensorRegistration(@RequestBody RegisterConfirmationDto registerConfirmation) {
        var result = this.registrationService.confirmSensorRegistration(registerConfirmation);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/measurements")
    public ResponseEntity<String> addMeasurement(@RequestBody BaseRawDataDto rawDataDto) {
        var result = this.measurementService.addMeasurements(rawDataDto);
        if (result) {
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("OK", HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(NoSensorRegistrationActiveException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
