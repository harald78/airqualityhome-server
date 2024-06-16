package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.dto.SensorDto;
import life.airqualityhome.server.service.SensorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/settings")
public class SettingsController {

    private final SensorService sensorService;

    public SettingsController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SensorDto> getSensorSettings(@PathVariable Long id) {
        var sensorDto = this.sensorService.getSensorDtoById(id);
        return new ResponseEntity<>(sensorDto, HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SensorDto> saveSensorSettings(@RequestBody SensorDto sensorDto) {
        var updatedDto = this.sensorService.saveSensorSettings(sensorDto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
