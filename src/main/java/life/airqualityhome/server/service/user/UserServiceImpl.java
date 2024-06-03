package life.airqualityhome.server.service.user;

import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.model.mapper.UserEntityMapper;
import life.airqualityhome.server.repositories.RefreshTokenRepository;
import life.airqualityhome.server.repositories.UserRepository;
import life.airqualityhome.server.rest.dto.ChangePasswordRequestDto;
import life.airqualityhome.server.rest.dto.ChangeUserRequestDto;
import life.airqualityhome.server.rest.dto.UserResponseDto;
import life.airqualityhome.server.rest.exceptions.PasswordRequiredException;
import life.airqualityhome.server.rest.exceptions.UserNotFoundException;
import life.airqualityhome.server.rest.exceptions.UsernameRequiredException;
import life.airqualityhome.server.rest.exceptions.WrongCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserEntityMapper userEntityMapper;

    private final AuthenticationManager authenticationManager;

    public UserServiceImpl (UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                            UserEntityMapper userEntityMapper, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userEntityMapper = userEntityMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserResponseDto saveUser(ChangeUserRequestDto userRequest) {
        return userRepository.findFirstById(userRequest.getId())
            .map(user -> {
                this.checkCredentials(user.getUsername(), userRequest.getPassword());
                user.setId(user.getId());
                user.setUsername(userRequest.getUsername());
                user.setEmail(userRequest.getEmail());
                user.setRoles(user.getRoles());
                UserEntity savedUser = userRepository.save(user);
                userRepository.refresh(savedUser);
                this.mayBeLogoutUser(savedUser, userRequest);
                return savedUser; })
            .map(userEntityMapper::toResponseDto)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE)); }

    @Override
    public UserResponseDto savePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        this.checkCredentials(changePasswordRequestDto.getUsername(), changePasswordRequestDto.getOldPassword());
        UserEntity response = Optional.ofNullable(changePasswordRequestDto.getId())
                                      .map(userRepository::findFirstById)
                                      .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE))
                                      .map(oldUser -> {
                                          BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                                          String rawPassword = changePasswordRequestDto.getPassword();
                                          String encodedPassword = encoder.encode(rawPassword);
                                          oldUser.setPassword(encodedPassword);
                                          UserEntity savedUser = userRepository.save(oldUser);
                                          userRepository.refresh(savedUser);
                                          this.logoutUser(savedUser.getId());
                                          return savedUser;
                                      }).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        return userEntityMapper.toResponseDto(response);
    }

    private void mayBeLogoutUser(UserEntity savedUser, ChangeUserRequestDto userRequestDto) {
        if (!savedUser.getUsername().equals(userRequestDto.getUsername())) {
            this.logoutUser(savedUser.getId());
        }
    }

    private void checkCredentials(String username, String password) {
        if(username == null){
            throw new UsernameRequiredException();
        } else if(password == null){
            throw new PasswordRequiredException();
        }

        boolean isAuthenticated = this.isAuthenticated(username, password);
        if (!isAuthenticated) {
            throw new WrongCredentialsException();
        }
    }

    @Override
    public UserEntity getUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        return userRepository.findByUsername(usernameFromAccessToken)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public UserResponseDto getUser() {
        UserEntity user = getUserEntity();
        return userEntityMapper.toResponseDto(user);
    }


    @Override
    public UserResponseDto getUserByUserName(String username) {
        return userRepository.findByUsername(username)
            .map(userEntityMapper::toResponseDto)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public UserResponseDto logoutUser(Long id) {
    return userRepository.findFirstById(id)
             .map(user -> {
                 refreshTokenRepository.findAllByUserInfo(user)
                                       .ifPresent(refreshTokenRepository::deleteAll);
                 return user; })
             .map(userEntityMapper::toResponseDto).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }


    @Override
    public UserResponseDto logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        return userRepository.findByUsername(usernameFromAccessToken)
            .map(user -> {
                refreshTokenRepository.findAllByUserInfo(user)
                    .ifPresent(refreshTokenRepository::deleteAll);
                return user; })
            .map(userEntityMapper::toResponseDto).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        return users.stream().map(u -> userEntityMapper.toResponseDto(u)).toList();
    }

    @Override
    public boolean isAuthenticated(final String username, final String password) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return authentication.isAuthenticated();
    }


}