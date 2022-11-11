package product.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import product.dto.user.EmailValidDto;
import product.dto.user.LoginRequestDto;
import product.dto.user.SignUpRequestDto;
import product.dto.user.TokenRequestDto;
import product.response.Response;
import product.service.user.AuthService;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static product.response.Response.success;


@Controller
@Slf4j
////////////////////////////////////////////
@RequiredArgsConstructor
//@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;


    Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup() {
        logger.info("회원가입 페이지로 이동");
        return "signup.html";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        logger.info("로그인 페이지로 이동");
        return "login.html";
    }

///////////////////////////////////////////////////////////////////////////////////////
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
