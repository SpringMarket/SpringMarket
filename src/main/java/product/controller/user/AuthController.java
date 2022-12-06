package product.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import product.response.Failure;
import product.response.Response;
import product.response.Success;
import product.service.user.AuthService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static product.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    Logger log = LoggerFactory.getLogger("ACCESS");


    @ApiResponses(value ={
            @ApiResponse(responseCode =  "200", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = Response.class)))
            ,@ApiResponse(responseCode = "400", description = "이메일/비밀번호/연령대 입력 형식 오류", content = @Content(schema = @Schema(implementation = Response.class)))
            ,@ApiResponse(responseCode = "409", description = "이미 가입된 회원", content = @Content(schema = @Schema(implementation = Response.class)))
            })
    @Operation(summary = "신규 회원 가입")
    @PostMapping("/signup")
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        log.info(signUpRequestDto.getEmail() + " 님이 가입하셨습니다.");
        return success();
    }

    @ApiResponses(value ={
            @ApiResponse(responseCode =  "200",
                    description = "로그인 성공",
                    headers = {@Header(name="AccessToken"),@Header(name="RefreshToken")})
            ,@ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
    })
    @Operation(summary = "회원 로그인")
    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        log.info(loginRequestDto.getEmail() + " 님이 로그인하셨습니다.");
        return success(authService.login(loginRequestDto, response));
    }

    @Operation(summary = "토큰 재발급")

    @ApiResponses(value ={
            @ApiResponse(responseCode =  "200",
                    description = "로그인 성공",
                    headers = {@Header(name="AccessToken"),@Header(name="RefreshToken")})
            ,@ApiResponse(responseCode = "401", description = "refreshtoken이 유효하지 않음")
    })
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        return success(authService.reissue(tokenRequestDto, response));
    }
}
