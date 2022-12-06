package product.controller.user;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import product.dto.user.LoginRequestDto;
import product.dto.user.SignUpRequestDto;
import product.dto.user.TokenRequestDto;
import product.response.Response;
import product.service.user.AuthService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static product.response.Response.success;

@ApiResponses({
        @ApiResponse(code = 200, message = "성공입니다.")
        ,@ApiResponse(code = 401, message = "유효한 인증 정보가 없습니다.")
        , @ApiResponse(code = 404, message = "요청하신 정보를 찾을 수 없습니다.")
        , @ApiResponse(code = 409, message = "이미 값이 존재합니다.")
        ,@ApiResponse(code = 500, message = "Internal Server Error")
})

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    Logger log = LoggerFactory.getLogger("ACCESS");


    @ApiOperation(value = "신규 회원 가입")
    @PostMapping("/signup")
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        log.info(signUpRequestDto.getEmail() + " 님이 가입하셨습니다.");
        return success();
    }

    @ApiOperation(value = "회원 로그인")
    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        log.info(loginRequestDto.getEmail() + " 님이 로그인하셨습니다.");
        return success(authService.login(loginRequestDto, response));
    }

    @ApiOperation(value = "토큰 재발급")
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        return success(authService.reissue(tokenRequestDto, response));
    }
}
