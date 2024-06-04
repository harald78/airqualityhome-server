package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.mapper.BaseRawDataDto;
import life.airqualityhome.server.service.measurement.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementsController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementsController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping
    public ResponseEntity<String> addMeasurement(@RequestBody BaseRawDataDto rawDataDto) {
        var result = this.measurementService.addMeasurements(rawDataDto);
        if (result) {
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        }
            return new ResponseEntity<>("OK", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<LatestMeasurementDto>> getUserMeasurements(@PathVariable String id) {
        var measurements = measurementService.getUserMeasurements(id);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @GetMapping("/sensor/{id}")
    public String getSensorMeasurements() {
        return "Hello Measurements";
    }

    @DeleteMapping("/user/{id}")
    public String deleteUserMeasurements() {
        return "Hello Measurements";
    }

    @DeleteMapping("/sensor/{id}")
    public String deleteSensorMeasurements() {
        return "Hello Measurements";
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
