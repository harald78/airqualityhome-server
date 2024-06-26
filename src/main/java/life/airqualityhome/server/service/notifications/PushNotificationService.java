package life.airqualityhome.server.service.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import life.airqualityhome.server.config.ApplicationProperties;
import life.airqualityhome.server.model.MeasurementViolationEntity;
import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.model.PushSubscriptionEntity;
import life.airqualityhome.server.repositories.PushSubscriptionRepository;
import life.airqualityhome.server.rest.dto.PushNotificationAction;
import life.airqualityhome.server.rest.dto.PushNotificationPayload;
import life.airqualityhome.server.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class PushNotificationService {

    private final PushSubscriptionRepository pushSubscriptionRepository;

    private final ApplicationProperties applicationProperties;

    private final SensorService sensorService;

    private PushService pushService;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        this.pushService = new PushService();
        this.pushService.setPublicKey(this.applicationProperties.getVapidPublicKey());
        this.pushService.setPrivateKey(this.applicationProperties.getVapidPrivateKey());
        this.pushService.setSubject("mailto:airqualityhome@gmx.net");
    }


    public PushNotificationService(PushSubscriptionRepository pushSubscriptionRepository,
                                   ApplicationProperties applicationProperties, SensorService sensorService) throws Exception {
        this.pushSubscriptionRepository = pushSubscriptionRepository;
        this.applicationProperties = applicationProperties;
        this.sensorService = sensorService;
    }

    public String getPublicKey() {
        return this.applicationProperties.getVapidPublicKey();
    }

    public String subscribe(final Subscription subscription, Long id) {
        PushSubscriptionEntity sub = this.pushSubscriptionRepository.findByUserId(id)
                .orElse(PushSubscriptionEntity.builder()
                        .build());

        sub.setPublicKey(subscription.keys.p256dh);
        sub.setAuth(subscription.keys.auth);
        sub.setEndpoint(subscription.endpoint);
        sub.setUserId(id);

        this.pushSubscriptionRepository.save(sub);
        return "OK";
    }


    public void maybeSendPushNotification(NotificationEntity notificationEntity, MeasurementViolationEntity mv) {

        Optional<PushSubscriptionEntity> pushSubscriptionEntity = this.pushSubscriptionRepository
                .findByUserId(notificationEntity.getUserId());

        if (pushSubscriptionEntity.isPresent() && this.applicationProperties.isActivatePushNotifications()) {
            var sub = pushSubscriptionEntity.get();
            var sensor = this.sensorService.getSensorEntityById(mv.getSensorId());

            var payload = PushNotificationPayload.builder()
                    .title(sensor.getLocation() + " - " + sensor.getSensorBaseSensorType().getSensorType().getType().name())
                    .body(notificationEntity.getMessage())
                    .icon("assets/icons/icon-192x192.png")
                    .silent(false)
                    .vibrate(List.of(100, 50, 100))
                    .data(Map.of(
                                "title", sensor.getLocation() + " - " + sensor.getSensorBaseSensorType().getSensorType().getType().name(),
                            "body", notificationEntity.getMessage(),
                            "icon", "assets/icons/icon-192x192.png",
                            "vibrate", List.of(100, 50, 100).toString(),
                            "silent", "false",
                            "url", "https://app.airqualityhome.life/notifications/"
                    ))
                    .actions(List.of(
                            PushNotificationAction.builder()
                                    .action("openWindow")
                                    .title("Open App")
                                    .build())).build();

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                Subscription webSub = new Subscription(sub.getEndpoint(), new Subscription.Keys(sub.getPublicKey(), sub.getAuth()));
                String payloadString = objectMapper.writeValueAsString(Map.of("notification", payload));
                Notification notification = new Notification(webSub, payloadString);
                HttpResponse response = this.pushService.send(notification);
                log.info("Send push notification {} to user {} with status {}", payloadString, notificationEntity.getUserId(), response.getStatusLine().getStatusCode());

            } catch (JoseException | ExecutionException | GeneralSecurityException | IOException |
                     InterruptedException e) {
                log.info("Error occurred, could not send push notification for notification {} reason {}", notificationEntity.getId(), e.getMessage());
            }

        } else {
            log.info("No push subscription found for user {}", notificationEntity.getUserId());
        }
    }

}
