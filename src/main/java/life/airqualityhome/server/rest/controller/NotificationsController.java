package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.exceptions.NoContentFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import life.airqualityhome.server.service.notifications.NotificationService;
import life.airqualityhome.server.rest.dto.NotificationDto;
import java.util.List;

@RestController
@RequestMapping("/api/app/notifications")
public class NotificationsController {

    private final NotificationService service;

    public NotificationsController(NotificationService service) {
        this.service = service;
    }

    @PostMapping(value = "/read/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationDto> readNotification(@PathVariable Long id) {
        var notificationDto = this.service.setNotificationRead(id);
        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }

    @GetMapping(value ="/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NotificationDto>> getUserNotifications(@PathVariable Long id) {
        List<NotificationDto> notifications = service.getAllUserNotifications(id);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteUserNotifications(@PathVariable Long id) {
        this.service.deleteUserNotifications(id);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> noContentFoundException(NoContentFoundException ex) { return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT); }

    @ExceptionHandler
    public ResponseEntity<String> illegalStateHandler(IllegalStateException ex) { return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); }
}
