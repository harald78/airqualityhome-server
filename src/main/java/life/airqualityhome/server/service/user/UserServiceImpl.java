package life.airqualityhome.server.service.user;

import life.airqualityhome.server.model.RefreshTokenEntity;
import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.model.mapper.UserEntityMapper;
import life.airqualityhome.server.repositories.RefreshTokenRepository;
import life.airqualityhome.server.repositories.UserRepository;
import life.airqualityhome.server.rest.dto.UserRequestDto;
import life.airqualityhome.server.rest.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserEntityMapper userEntityMapper;



    @Override
    public UserResponseDto saveUser(UserRequestDto userRequest) {
        if(userRequest.getUsername() == null){
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if(userRequest.getPassword() == null){
            throw new RuntimeException("Parameter password is not found in request..!!");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();

        UserEntity currentUser = userRepository.findByUsername(usernameFromAccessToken);

        UserEntity savedUser = null;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = userRequest.getPassword();
        String encodedPassword = encoder.encode(rawPassword);

        UserEntity user = userEntityMapper.toEntity(userRequest);
        user.setPassword(encodedPassword);
        if(userRequest.getId() != null){
            UserEntity oldUser = userRepository.findFirstById(userRequest.getId());
            if(oldUser != null){
                oldUser.setId(user.getId());
                oldUser.setPassword(user.getPassword());
                oldUser.setUsername(user.getUsername());
                oldUser.setRoles(user.getRoles());

                savedUser = userRepository.save(oldUser);
                userRepository.refresh(savedUser);
            } else {
                throw new RuntimeException("Can't find record with identifier: " + userRequest.getId());
            }
        } else {
//            user.setCreatedBy(currentUser);
            savedUser = userRepository.save(user);
        }
        userRepository.refresh(savedUser);
        UserResponseDto userResponse = userEntityMapper.toResponseDto(savedUser);
        return userResponse;
    }

    @Override
    public UserResponseDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        UserEntity user = userRepository.findByUsername(usernameFromAccessToken);
        UserResponseDto userResponse = userEntityMapper.toResponseDto(user);
        return userResponse;
    }

    @Override
    public UserResponseDto logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        UserEntity user = userRepository.findByUsername(usernameFromAccessToken);
        Optional<List<RefreshTokenEntity>> tokenList = refreshTokenRepository.findAllByUserInfo(user);
        tokenList.ifPresent(refreshTokenEntities -> refreshTokenRepository.deleteAll(refreshTokenEntities));
        return userEntityMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
//        Type setOfDTOsType = new TypeToken<List<UserResponseDto>>(){}.getType(); // TODO: Check for what it was needed
        return users.stream().map(u -> userEntityMapper.toResponseDto(u)).toList();
    }


}