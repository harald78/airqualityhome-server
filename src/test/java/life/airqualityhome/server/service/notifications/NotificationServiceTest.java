package life.airqualityhome.server.service.notifications;

import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.repositories.NotificationCRUDRepository;
import life.airqualityhome.server.rest.dto.NotificationDto;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapper;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    NotificationCRUDRepository notificationCRUDRepository;

    NotificationMapper notificationMapper;

    NotificationService sut;

    @BeforeEach
    void setUp() {
        this.notificationMapper = new NotificationMapperImpl();
        this.sut = new NotificationService(notificationCRUDRepository, notificationMapper);
    }

    @Test
    void getAllUserNotifications_shouldReturnEmptyList() {
        // given
        Long userId = 1L;

        // when
        when(notificationCRUDRepository.findAllByUserId(anyLong())).thenReturn(List.of());
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
        when(notificationCRUDRepository.findAllByUserId(anyLong())).thenReturn(notificationEntities);
        var result = sut.getAllUserNotifications(userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(NotificationDto.class, result.get(0).getClass());
    }
}