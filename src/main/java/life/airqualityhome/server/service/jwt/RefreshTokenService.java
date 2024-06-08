package life.airqualityhome.server.service.jwt;

import life.airqualityhome.server.model.RefreshTokenEntity;
import life.airqualityhome.server.repositories.RefreshTokenRepository;
import life.airqualityhome.server.repositories.UserRepository;
import life.airqualityhome.server.rest.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;

    UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshTokenEntity createRefreshToken(String username) {
        return userRepository.findByUsername(username)
            .map(user -> RefreshTokenEntity.builder()
                                           .userInfo(user)
                                           .token(UUID.randomUUID().toString())
                                           .expiryDate(Instant.now().plusMillis(1000 * 60 * 60 * 12)) // max login validity = 12h
                                           .build())
            .map(token -> refreshTokenRepository.save(token))
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;

    }
}

