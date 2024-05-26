package life.airqualityhome.server.rest.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/login")
    public String loginUser() {
        return "Hello User";
    }
}
