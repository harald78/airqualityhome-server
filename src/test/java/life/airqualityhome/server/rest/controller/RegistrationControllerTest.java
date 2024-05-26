package life.airqualityhome.server.rest.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationControllerTest {

    @PostMapping("/sensor")
    public String addSensorRegistration() {
        return "Hello RegistrationSensor";
    }

    @PostMapping("/sensor/confirm")
    public String confirmSensorRegistration() {
        return "Hello RegistrationSensorConfirm";
    }
}
