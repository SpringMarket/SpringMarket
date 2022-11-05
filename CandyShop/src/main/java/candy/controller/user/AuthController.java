package candy.controller.user;

import candy.dto.user.*;
import candy.response.Response;
import candy.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return Response.success();
    }

    @PostMapping("/id-duplicate")
    public Response emailDuplicate(@RequestBody EmailValidDto emailValidDto) {
        authService.emailDuplicate(emailValidDto);
        return Response.success();
    }

    @PostMapping("/name-duplicate")
    public Response nameDuplicate(@RequestBody NameValidDto nameValidDto) {
        authService.nameDuplicate(nameValidDto);
        return Response.success();
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return Response.success(authService.login(loginRequestDto, response));
    }


    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        return Response.success(authService.reissue(tokenRequestDto, response));
    }
}
