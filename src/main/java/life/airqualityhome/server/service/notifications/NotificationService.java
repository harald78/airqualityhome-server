package life.airqualityhome.server.service.notifications;

import life.airqualityhome.server.repositories.NotificationCRUDRepository;
import life.airqualityhome.server.rest.dto.NotificationDto;
import life.airqualityhome.server.rest.dto.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    public final NotificationCRUDRepository repository;

    public final NotificationMapper mapper;

    public NotificationService(NotificationCRUDRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<NotificationDto> getAllUserNotifications(Long userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}