package life.airqualityhome.server.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import life.airqualityhome.server.service.notifications.NotificationService;
import life.airqualityhome.server.rest.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
@RequestMapping("/api/app/notifications")
public class NotificationsController {

    @Autowired
    private NotificationService service;

    @PostMapping("/read/{id}")
    public String readNotification() {
        return "Hello Notifications";
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<NotificationDto>> getUserNotifications(@PathVariable Long id) {
        List<NotificationDto> notifications = service.getAllUserNotifications(id);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUserNotifications() {
        return "Hello Notifications";
    }

    @DeleteMapping("/{id}")
    public String deleteNotifications() {
        return "Hello Notifications";
    }
}
