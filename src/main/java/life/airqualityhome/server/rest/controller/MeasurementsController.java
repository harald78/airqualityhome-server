package life.airqualityhome.server.rest.controller;

import jakarta.websocket.server.PathParam;
import life.airqualityhome.server.rest.dto.HistoryMeasurementDto;
import life.airqualityhome.server.rest.dto.LatestMeasurementDto;
import life.airqualityhome.server.rest.dto.BaseRawDataDto;
import life.airqualityhome.server.rest.exceptions.NoContentFoundException;
import life.airqualityhome.server.service.measurement.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/app/measurements")
public class MeasurementsController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementsController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LatestMeasurementDto>> getUserMeasurements(@PathVariable String id) {
        var measurements = measurementService.getUserMeasurements(id);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @GetMapping(value = "/sensor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoryMeasurementDto> getSensorMeasurements(@PathVariable Long id, @PathParam("from") Instant from, @PathParam("to") Instant to) {
        var measurements = measurementService.getSensorMeasurements(id, from, to);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @GetMapping(value = "/base/{id}/{userid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoryMeasurementDto> getBaseMeasurements(@PathVariable Long id, @PathVariable Long userid, @PathParam("from") Instant from, @PathParam("to") Instant to) {
        var measurements = measurementService.getBaseMeasurements(userid, id, from, to);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
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
    public ResponseEntity<String> noContentFoundException(NoContentFoundException ex) { return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT); }
}
