package life.airqualityhome.server.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="airqualityhome")
public class ApplicationProperties {

    private String jwtSecret;

    private String tokenExpireMillis;

    private String sensorApiTokenHeaderName;

    private String sensorApiAuthToken;

    private Integer maxNotificationIntervalMinutes;

    private Integer maxSensorMeasurementIntervalMinutes;

    private String vapidPublicKey;

    private String vapidPrivateKey;

    private boolean activatePushNotifications;
}
