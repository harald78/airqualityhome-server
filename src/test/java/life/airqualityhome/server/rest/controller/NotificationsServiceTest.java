package life.airqualityhome.server.rest.controller;

import life.airqualityhome.server.model.NotificationEntity;
import life.airqualityhome.server.repositories.NotificationCRUDRepository;
import life.airqualityhome.server.rest.dto.NotificationDto;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapper;
import life.airqualityhome.server.service.notifications.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class NotificationsServiceTest {

private static final Logger logger = LoggerFactory.getLogger(NotificationsServiceTest.class);

    @Mock
    private NotificationCRUDRepository repositoryMock;

    @Mock
    private NotificationMapper mapperMock;

    private NotificationService sut;

    @BeforeEach
    public void init() {
//        try (Closeable theMock = (Closeable) MockitoAnnotations.openMocks(this)) {
//
//        } catch (Exception e) {
//            logger.error("Error initializing mocks", e);
//        }

        sut = new NotificationService(repositoryMock, mapperMock);
    }

    @Test
    public void getTestNotifications() {
        Long userId = 1L;
        NotificationEntity notificationEntity = NotificationEntity.builder().message("Test").build();
        List<NotificationEntity> mockNotifications = List.of(notificationEntity);

        when(repositoryMock.findAllByUserId(userId)).thenReturn(mockNotifications);
        when(mapperMock.toDto(notificationEntity)).thenReturn(new NotificationDto());

        List<NotificationDto> result = sut.getAllUserNotifications(userId);

        assertEquals(1, result.size());
        verify(repositoryMock, times(1)).findAllByUserId(userId);
    }
}