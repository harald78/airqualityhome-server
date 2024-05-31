package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.dto.SensorMeasurementDto;
import life.airqualityhome.server.service.measurement.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String addMeasurement() {
        return "Hello Measurements";
    }

    @GetMapping("/user/{id}")
    public List<SensorMeasurementDto> getUserMeasurements(@PathVariable String id) {
        return measurementService.getUserMeasurements(id);
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
}
