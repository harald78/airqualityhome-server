package life.airqualityhome.server.rest.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    @PostMapping("/read/{id}")
    public String readNotification() {
        return "Hello Notifications";
    }

    @GetMapping("/user/{id}")
    public String getUserNotifications() {
        return "Hello Notifications";
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
