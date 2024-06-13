package life.airqualityhome.server.service.notifications;

import life.airqualityhome.server.config.ApplicationProperties;
import life.airqualityhome.server.model.MeasurementViolationEntity;
import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.model.event.MeasurementViolationEvent;
import life.airqualityhome.server.repositories.NotificationCRUDRepository;
import life.airqualityhome.server.rest.dto.NotificationDto;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    private final NotificationCRUDRepository repository;

    private final NotificationMapper mapper;

    private final ApplicationProperties applicationProperties;

    public NotificationService(NotificationCRUDRepository repository, NotificationMapper mapper, ApplicationProperties applicationProperties) {
        this.repository = repository;
        this.mapper = mapper;
        this.applicationProperties = applicationProperties;
    }

    public List<NotificationDto> getAllUserNotifications(Long userId) {
        return repository.findAllByUserId(userId)
            .map(l -> l.stream().map(mapper::toDto).toList())
            .orElse(List.of());
    }

    public void deleteUserNotifications(Long userId) {
        var userNotifications = repository.findAllByUserId(userId)
                                          .map(l -> l.stream().map(NotificationEntity::getId).toList());
        userNotifications.ifPresent(repository::deleteAllById);
    }

    public NotificationDto setNotificationRead(Long notificationId) {
        var userNotification = repository.findById(notificationId);
        if (userNotification.isPresent()) {
            var notification = userNotification.get();
            notification.setRead(true);
            return this.mapper.toDto(this.repository.save(notification));
        } else {
            throw new IllegalStateException("User notification not found");
        }
    }

    @Async
    @EventListener
    public void maybeCreateNotification(MeasurementViolationEvent event) {
        var maxNotificationInterval = this.applicationProperties.getMaxNotificationIntervalMinutes();
        var userId = event.getViolationEntities().get(0).getUserId(); // Get userId from first element
        var timestamp = event.getTimestamp().minus(maxNotificationInterval, ChronoUnit.MINUTES);
        event.getViolationEntities().forEach(mv -> {
            this.repository.findByUserIdAndMeasurementEntity_SensorEntity_IdAndCreatedAfter(userId, mv.getSensorId(), timestamp)
                .ifPresentOrElse(notification -> log.info("Measurement violation for {}. Will not create notification because last notification was {}",
                                                mv.getSensorEntity().getSensorBaseSensorType().getSensorType().getType().name(), notification.getCreated().toString()),
                                 () -> { var userNotification = NotificationEntity.builder()
                                            .userId(userId)
                                            .read(false)
                                            .message(this.getViolationMessage(mv))
                                            .measurementEntityId(mv.getMeasurementEntityId())
                                            .build();
                    this.repository.save(userNotification);
                });
        });
    }

    private String getViolationMessage(MeasurementViolationEntity mve) {
        var type = mve.getType().name();
        var alarmValue = String.format("%.2f", mve.getAlarmValue());
        var diff = Math.abs(mve.getAlarmValue() - mve.getCorrectedValue());
        var formattedDiff = String.format("%.2f", diff);
        var location = mve.getSensorEntity().getLocation();
        return switch (mve.getSensorEntity().getSensorBaseSensorType().getSensorType().getType()) {
            case TEMPERATURE -> type + " temperature " + alarmValue + " °C violated by " + formattedDiff + " degrees in " + location;
            case HUMIDITY -> type + " humidity " + alarmValue + " % violated by " + formattedDiff + " percent in " + location;
            case PRESSURE -> type + " pressure " + alarmValue + " hPa violated by " + formattedDiff + " hPa in" + location;
            case GAS -> type + " gas " + alarmValue + " ppm violated by " + formattedDiff + " ppm in " + location;
            case PARTICLE -> type + " particle " + alarmValue + " ppm violated by " + formattedDiff + " ppm in " + location;
            case LIGHT -> type + " light " + alarmValue + " lx violated by " + formattedDiff + " lx in " + location;
        };
    }
}