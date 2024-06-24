package life.airqualityhome.server.service.notifications;

import life.airqualityhome.server.config.ApplicationProperties;
import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.repositories.NotificationCRUDRepository;
import life.airqualityhome.server.rest.dto.NotificationDto;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapper;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapperImpl;
import life.airqualityhome.server.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    NotificationCRUDRepository notificationCRUDRepository;

    @Mock
    SensorService sensorService;

    NotificationMapper notificationMapper;

    ApplicationProperties applicationProperties;

    NotificationService sut;

    @BeforeEach
    void setUp() {
        this.notificationMapper = new NotificationMapperImpl();
        this.applicationProperties = new ApplicationProperties();
        this.applicationProperties.setMaxNotificationIntervalMinutes(10);
        this.sut = new NotificationService(notificationCRUDRepository, notificationMapper, applicationProperties, sensorService);
    }

    @Test
    void getAllUserNotifications_shouldReturnEmptyList() {
        // given
        Long userId = 1L;

        // when
        when(notificationCRUDRepository.findAllByUserIdOrderByCreatedDesc(anyLong())).thenReturn(Optional.empty());
        var result = sut.getAllUserNotifications(userId);

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getAllUserNotifications_shouldReturnNotificationList() {
        // given
        Long userId = 1L;
        List<NotificationEntity> notificationEntities = List.of(
                NotificationEntity.builder().id(1L).userId(1L).message("Test Message 1").build(),
                NotificationEntity.builder().id(2L).userId(1L).message("Test Message 2").build()
        );

        // when
        when(notificationCRUDRepository.findAllByUserIdOrderByCreatedDesc(anyLong())).thenReturn(Optional.of(notificationEntities));
        var result = sut.getAllUserNotifications(userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(NotificationDto.class, result.get(0).getClass());
    }
}