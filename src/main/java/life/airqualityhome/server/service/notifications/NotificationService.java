package life.airqualityhome.server.service.notifications;

import life.airqualityhome.server.config.ApplicationProperties;
import life.airqualityhome.server.model.MeasurementViolationEntity;
import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.model.event.MeasurementViolationEvent;
import life.airqualityhome.server.repositories.NotificationCRUDRepository;
import life.airqualityhome.server.rest.dto.NotificationDto;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapper;
import life.airqualityhome.server.service.SensorService;
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

    private final SensorService sensorService;

    private final ApplicationProperties applicationProperties;

    public NotificationService(NotificationCRUDRepository repository, NotificationMapper mapper,
                               ApplicationProperties applicationProperties, SensorService sensorService) {
        this.repository = repository;
        this.mapper = mapper;
        this.applicationProperties = applicationProperties;
        this.sensorService = sensorService;
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
            notification.setAcknowledged(true);
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
            var sensorEntity = this.sensorService.getSensorEntityById(mv.getSensorId());
            this.repository.findByUserIdAndMeasurementEntity_SensorEntity_IdAndCreatedAfter(userId, mv.getSensorId(), timestamp)
                .ifPresentOrElse(notification -> log.info("Measurement violation for {}. Will not create notification because last notification was {}",
                                                sensorEntity.getSensorBaseSensorType().getSensorType().getType().name(), notification.getCreated().toString()),
                                 () -> { var userNotification = NotificationEntity.builder()
                                            .userId(userId)
                                            .acknowledged(false)
                                            .readAt(null)
                                            .message(this.getViolationMessage(mv, sensorEntity))
                                            .measurementEntityId(mv.getMeasurementEntityId())
                                            .build();
                    this.repository.save(userNotification);
                });
        });
    }

    private String getViolationMessage(MeasurementViolationEntity mve, SensorEntity sensorEntity) {
        var type = mve.getType().name();
        var alarmValue = String.format("%.2f", mve.getAlarmValue());
        var diff = Math.abs(mve.getAlarmValue() - mve.getCorrectedValue());
        var formattedDiff = String.format("%.2f", diff);
        return switch (sensorEntity.getSensorBaseSensorType().getSensorType().getType()) {
            case TEMPERATURE -> type + " temperature " + alarmValue + " °C violated by " + formattedDiff + " degrees";
            case HUMIDITY -> type + " humidity " + alarmValue + " % violated by " + formattedDiff + " percent";
            case PRESSURE -> type + " pressure " + alarmValue + " hPa violated by " + formattedDiff + " hPa";
            case GAS -> type + " gas " + alarmValue + " ppm violated by " + formattedDiff + " ppm";
            case PARTICLE -> type + " particle " + alarmValue + " ppm violated by " + formattedDiff + " ppm";
            case LIGHT -> type + " light " + alarmValue + " lx violated by " + formattedDiff + " lx";
        };
    }
}