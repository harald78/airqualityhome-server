package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.rest.exceptions.NoContentFoundException;
import life.airqualityhome.server.service.notifications.PushNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import life.airqualityhome.server.service.notifications.NotificationService;
import life.airqualityhome.server.rest.dto.NotificationDto;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/app/notifications")
public class NotificationsController {

    private final NotificationService service;
    private final PushNotificationService pushNotificationService;

    public NotificationsController(NotificationService service, PushNotificationService pushNotificationService) {
        this.service = service;
        this.pushNotificationService = pushNotificationService;
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

    @GetMapping(value = "/vapidPublicKey", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getPublicKey() {
        var vapidPublicKey = this.pushNotificationService.getPublicKey();
        return new ResponseEntity<>(vapidPublicKey, HttpStatus.OK);
    }

    @PostMapping(value = "/subscribe/{id}")
    public ResponseEntity<String> subscribe(@PathVariable Long id, @RequestBody Map<String, Object> subscription) {
        var status = this.pushNotificationService.subscribe(subscription, id);
        return new ResponseEntity<>(status, HttpStatus.OK);
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
