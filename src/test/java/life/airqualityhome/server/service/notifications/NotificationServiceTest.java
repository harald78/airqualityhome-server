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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationCRUDRepository repository;

    @Mock
    private NotificationMapper mapper;

    @Mock
    private SensorService sensorService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private NotificationService notificationService;

    @Nested
    class GetAllUserNotificationsTests {
        @Test
        void getAllUserNotifications_ShouldReturnNotificationDtoList() {
            Long userId = 1L;
            NotificationEntity notificationEntity = new NotificationEntity();
            NotificationDto notificationDto = new NotificationDto();

            when(repository.findAllByUserId(userId)).thenReturn(Optional.of(List.of(notificationEntity)));
            when(mapper.toDto(notificationEntity)).thenReturn(notificationDto);

            List<NotificationDto> result = notificationService.getAllUserNotifications(userId);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(notificationDto, result.get(0));
        }
    }

    @Nested
    class DeleteUserNotificationsTests {
        @Test
        void deleteUserNotifications_ShouldDeleteNotifications() {
            Long userId = 1L;
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setId(1L);

            when(repository.findAllByUserId(userId)).thenReturn(Optional.of(List.of(notificationEntity)));

            notificationService.deleteUserNotifications(userId);

            verify(repository, times(1)).deleteAllById(List.of(1L));
        }
    }

    @Nested
    class SetNotificationReadTests {
        @Test
        void setNotificationRead_ShouldSetNotificationAsRead() {
            Long notificationId = 1L;
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setAcknowledged(false);
            NotificationDto notificationDto = new NotificationDto();

            when(repository.findById(notificationId)).thenReturn(Optional.of(notificationEntity));
            when(repository.save(any(NotificationEntity.class))).thenReturn(notificationEntity);
            when(mapper.toDto(notificationEntity)).thenReturn(notificationDto);

            NotificationDto result = notificationService.setNotificationRead(notificationId);

            assertNotNull(result);
            assertTrue(notificationEntity.getAcknowledged());
            verify(repository, times(1)).save(notificationEntity);
        }

        @Test
        void setNotificationRead_ShouldThrowExceptionIfNotFound() {
            Long notificationId = 1L;

            when(repository.findById(notificationId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> notificationService.setNotificationRead(notificationId));
        }
    }

    @Nested
    class MaybeCreateNotificationTests {
        @Test
        void maybeCreateNotification_ShouldCreateNotification() {
            Long userId = 1L;
            Long sensorId = 1L;
            Instant eventTimestamp = Instant.now();
            int maxNotificationInterval = 30;
            MeasurementViolationEntity violationEntity = new MeasurementViolationEntity();
            violationEntity.setSensorId(sensorId);
            violationEntity.setUserId(userId);
            violationEntity.setMeasurementEntityId(1L);

            SensorEntity sensorEntity = new SensorEntity();
            MeasurementViolationEvent event = new MeasurementViolationEvent(List.of(violationEntity), eventTimestamp);

            when(applicationProperties.getMaxNotificationIntervalMinutes()).thenReturn(maxNotificationInterval);
            when(sensorService.getSensorEntityById(sensorId)).thenReturn(sensorEntity);
            when(repository.findByUserIdAndMeasurementEntity_SensorEntity_IdAndCreatedAfter(eq(userId), eq(sensorId), any(Instant.class)))
                    .thenReturn(Optional.empty());

            notificationService.maybeCreateNotification(event);

            ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
            verify(repository, times(1)).save(notificationCaptor.capture());

            NotificationEntity savedNotification = notificationCaptor.getValue();
            assertEquals(userId, savedNotification.getUserId());
            assertFalse(savedNotification.getAcknowledged());
            assertEquals(violationEntity.getMeasurementEntityId(), savedNotification.getMeasurementEntityId());
        }

        @Test
        void maybeCreateNotification_ShouldNotCreateNotificationIfRecentExists() {
            Long userId = 1L;
            Long sensorId = 1L;
            Instant eventTimestamp = Instant.now();
            int maxNotificationInterval = 30;
            MeasurementViolationEntity violationEntity = new MeasurementViolationEntity();
            violationEntity.setSensorId(sensorId);
            violationEntity.setUserId(userId);
            violationEntity.setMeasurementEntityId(1L);

            SensorEntity sensorEntity = new SensorEntity();
            MeasurementViolationEvent event = new MeasurementViolationEvent(List.of(violationEntity), eventTimestamp);

            NotificationEntity existingNotification = new NotificationEntity();

            when(applicationProperties.getMaxNotificationIntervalMinutes()).thenReturn(maxNotificationInterval);
            when(sensorService.getSensorEntityById(sensorId)).thenReturn(sensorEntity);
            when(repository.findByUserIdAndMeasurementEntity_SensorEntity_IdAndCreatedAfter(eq(userId), eq(sensorId), any(Instant.class)))
                    .thenReturn(Optional.of(existingNotification));

            notificationService.maybeCreateNotification(event);

            verify(repository, never()).save(any(NotificationEntity.class));
        }
    }
}