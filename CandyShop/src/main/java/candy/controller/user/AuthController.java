package candy.controller.user;

import candy.dto.user.EmailValidDto;
import candy.dto.user.LoginRequestDto;
import candy.dto.user.SignUpRequestDto;
import candy.dto.user.TokenRequestDto;
import candy.response.Response;
import candy.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static candy.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;



    @PostMapping("/signup")
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return success();
    }

    @PostMapping("/id-duplicate")
    public Response emailDuplicate(@RequestBody EmailValidDto emailValidDto) {
        authService.emailDuplicate(emailValidDto);
        return success();
    }


    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return success(authService.login(loginRequestDto, response));
    }

    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        return success(authService.reissue(tokenRequestDto, response));
    }
}
