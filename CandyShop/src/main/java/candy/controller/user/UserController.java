package candy.controller.user;


import candy.exception.ExceptionType;
import candy.repository.user.UserRepository;
import candy.response.Response;
import candy.service.user.UserService;
import candy.dto.user.UserDto;
import candy.entity.user.User;
import candy.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public Response findAllUsers() {
        return Response.success(userService.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public Response findUser(@PathVariable int id) {
        return Response.success(userService.findUser(id));
    }

    @PutMapping("/users/{id}")
    public Response editUserInfo(@PathVariable int id, @RequestBody UserDto userDto) {
        return Response.success(userService.editUserInfo(id, userDto));
    }

    @DeleteMapping("/users/{id}")
    public Response deleteUserInfo(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION));

        userService.deleteUserInfo(user, id);
        return Response.success();
    }
}