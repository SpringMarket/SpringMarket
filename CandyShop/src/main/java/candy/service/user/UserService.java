package candy.service.user;

import candy.exception.ExceptionType;
import candy.repository.user.UserRepository;
import candy.exception.RequestException;
import candy.dto.user.UserDto;
import candy.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(UserDto.toDto(user));
        }
        return userDtos;
    }

    @Transactional(readOnly = true)
    public UserDto findUser(int id) {
        return UserDto.toDto(userRepository.findById(id).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION)));

    }


    @Transactional
    public UserDto editUserInfo(int id, UserDto updateInfo) {
        User user = userRepository.findById(id).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));

        // 권한 처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getName().equals(user.getEmail())) {
            throw new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION);
        } else {
            user.setName(updateInfo.getName());
            return UserDto.toDto(user);
        }
    }

    @Transactional
    public void deleteUserInfo(User user, int id) {
        User target = userRepository.findById(id).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));

        if (user.equals(target)) {
            userRepository.deleteById(id);
        } else {
            throw new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION);
        }
    }

    @Transactional
    public void deleteAll(User user) {

    }
}

