package life.airqualityhome.server.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import life.airqualityhome.server.service.apitoken.SensorApiTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class SensorApiTokenFilter extends OncePerRequestFilter {

    private final SensorApiTokenService sensorApiTokenService;

    public SensorApiTokenFilter(SensorApiTokenService sensorApiTokenService) {
        this.sensorApiTokenService = sensorApiTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Bypass filter if it is not a sensor request
        if (!request.getRequestURI().contains("/api/sensor/")) {
            log.debug("Bypass filter...");
            filterChain.doFilter(request, response);

        } else {

            try {
                Authentication authentication = this.sensorApiTokenService.getAuthentication(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (BadCredentialsException e) {
                log.error(e.getMessage());
            }

            log.debug("Do not bypass filter...");
            filterChain.doFilter(request, response);
        }
    }
}
