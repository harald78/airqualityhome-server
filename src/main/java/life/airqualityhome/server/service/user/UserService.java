package life.airqualityhome.server.service.user;

import life.airqualityhome.server.rest.dto.UserRequestDto;
import life.airqualityhome.server.rest.dto.UserResponseDto;

import java.util.List;


public interface UserService {

    UserResponseDto saveUser(UserRequestDto userRequest);

    UserResponseDto getUser();

    List<UserResponseDto> getAllUser();


}