package life.airqualityhome.server.rest.controller;

import io.jsonwebtoken.ExpiredJwtException;
import life.airqualityhome.server.model.RefreshTokenEntity;
import life.airqualityhome.server.rest.dto.*;
import life.airqualityhome.server.service.jwt.JwtService;
import life.airqualityhome.server.service.jwt.RefreshTokenService;
import life.airqualityhome.server.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/user")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping(value = "/save")
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody ChangeUserRequestDto userRequest) {
        try {
            UserResponseDto userResponse = userService.saveUser(userRequest);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/save-password")
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody ChangePasswordRequestDto userRequest) {
        try {
            UserResponseDto userResponse = userService.savePassword(userRequest);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserProfile() {
        try {
            UserResponseDto userResponse = userService.getUser();
            return ResponseEntity.ok().body(userResponse);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<UserResponseDto> logoutUser() {
        try {
            UserResponseDto userResponse = userService.logoutUser();
            return ResponseEntity.ok().body(userResponse);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDTO){
        boolean authenticated = this.userService.isAuthenticated(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        if(authenticated){
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            var jwtDto = JwtResponseDto.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken()).build();
            return new ResponseEntity<>(jwtDto, HttpStatus.OK);

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    var jwtDto = JwtResponseDto.builder()
                                               .accessToken(accessToken)
                                               .token(refreshTokenRequestDTO.getToken()).build();
                    return new ResponseEntity<>(jwtDto, HttpStatus.OK);
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(ExpiredJwtException e){
        return ResponseEntity.status(401).build();
    }
}
