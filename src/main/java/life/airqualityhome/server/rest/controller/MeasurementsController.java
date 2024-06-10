package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.dto.HistoryMeasurementDto;
import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.BaseRawDataDto;
import life.airqualityhome.server.service.measurement.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/measurements")
public class MeasurementsController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementsController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<LatestMeasurementDto>> getUserMeasurements(@PathVariable String id) {
        var measurements = measurementService.getUserMeasurements(id);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @GetMapping("/sensor/{id}")
    public ResponseEntity<HistoryMeasurementDto> getSensorMeasurements(@PathVariable Long id) {
        var measurements = measurementService.getSensorMeasurements(id);
        return new ResponseEntity<>(measurements, HttpStatus.OK);

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
