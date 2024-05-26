package life.airqualityhome.server.rest.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementsControllerTest {

    @PostMapping
    public String addMeasurement() {
        return "Hello Measurements";
    }

    @GetMapping("/user/{id}")
    public String getUserMeasurements() {
        return "Hello Measurements";
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
