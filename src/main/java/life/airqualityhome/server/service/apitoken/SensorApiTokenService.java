package life.airqualityhome.server.service.apitoken;

import jakarta.servlet.http.HttpServletRequest;
import life.airqualityhome.server.config.ApplicationProperties;
import life.airqualityhome.server.model.apitoken.SensorApiKeyAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Enumeration;

@Slf4j
@Service
public class SensorApiTokenService {

    private final ApplicationProperties applicationProperties;

    public SensorApiTokenService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiToken = request.getHeader(applicationProperties.getSensorApiTokenHeaderName());

        if (apiToken == null || !apiToken.equals(applicationProperties.getSensorApiAuthToken())) {
            throw new BadCredentialsException("Invalid API Token");
        }

        return new SensorApiKeyAuthentication(apiToken, AuthorityUtils.NO_AUTHORITIES);
    }

}
