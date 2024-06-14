package life.airqualityhome.server.service.user;

import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.rest.dto.ChangePasswordRequestDto;
import life.airqualityhome.server.rest.dto.ChangeUserRequestDto;
import life.airqualityhome.server.rest.dto.UserResponseDto;

import java.util.List;


public interface UserService {

    UserResponseDto saveUser(ChangeUserRequestDto userRequest);

    UserResponseDto getUser();

    UserResponseDto getUserByUserName(String userName);

    UserResponseDto savePassword(ChangePasswordRequestDto changePasswordRequestDto);

    UserEntity getUserEntity();

    UserResponseDto logoutUser(Long id);

    UserResponseDto logoutUser();

    List<UserResponseDto> getAllUser();

    boolean isAuthenticated(String username, String password);


}