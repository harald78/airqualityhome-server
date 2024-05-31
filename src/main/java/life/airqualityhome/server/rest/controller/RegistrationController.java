package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.dto.SensorBaseDto;
import life.airqualityhome.server.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/sensor")
    public String addSensorRegistration() {
        return "Hello RegistrationSensor";
    }

    @GetMapping("/sensorBase")
    public ResponseEntity<List<SensorBaseDto>> getAvailableSensorBases() {
        final var sensorBases = this.registrationService.getAvailableSensorBases();
        return new ResponseEntity<>(sensorBases, HttpStatus.OK);
    }

    @PostMapping("/sensor/confirm")
    public String confirmSensorRegistration() {
        return "Hello RegistrationSensorConfirm";
    }
}
