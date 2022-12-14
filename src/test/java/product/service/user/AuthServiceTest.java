package product.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import product.MysqlTestContainer;
import product.config.jwt.TokenProvider;
import product.dto.user.LoginRequestDto;
import product.dto.user.SignUpRequestDto;
import product.dto.user.TokenRequestDto;
import product.dto.user.TokenResponseDto;
import product.entity.user.Authority;
import product.entity.user.User;
import product.exception.RequestException;
import product.repository.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthServiceTest extends MysqlTestContainer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthRedisService authRedisService;


    @Test
    @DisplayName("<1> 회원가입 -> 성공")
    void signupSuccess() {
        // GIVEN
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("suyoung@naver.com","1234","20대");

        // WHEN
        authService.signup(signUpRequestDto);

        // THEN
        User user = userRepository.findByEmail("suyoung@naver.com").orElse(null);
        assert user != null;
        assertThat(user.getEmail()).isEqualTo("suyoung@naver.com");
    }

    @Test
    @DisplayName("<2> 회원가입 -> ALREADY_EXISTS")
    void signupEmailExist() {
        // GIVEN
        userRepository.save(User.builder()
                .email("suyoung@naver.com")
                .password(passwordEncoder.encode("password"))
                .age("30대")
                .authority(Authority.ROLE_USER)
                .build());

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("suyoung@naver.com","1234","20대");

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            authService.signup(signUpRequestDto); });
        String message = exception.getMessage();

        // THEN
        assertThat(message).isEqualTo("이미 값이 존재합니다.");
    }

    @Test
    @DisplayName("<3> 로그인 -> 성공")
    void loginSuccess() {
        // GIVEN
        userRepository.save(User.builder()
                .email("suyoung@naver.com")
                .password(passwordEncoder.encode("1234"))
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build());
        LoginRequestDto loginRequestDto = new LoginRequestDto("suyoung@naver.com","1234");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // WHEN
        TokenResponseDto dto = authService.login(loginRequestDto,response);

        // THEN
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("<4> 로그인 -> NOT_FOUND_USER")
    void loginNotFound() {
        // GIVEN
        userRepository.save(User.builder()
                .email("suyoung@naver.com")
                .password(passwordEncoder.encode("1234"))
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build());

        LoginRequestDto loginRequestDto = new LoginRequestDto("test@naver.com","1234");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            authService.login(loginRequestDto,response); });
        String message = exception.getMessage();

        // THEN
        assertThat(message).isEqualTo("요청하신 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("<5> 로그인 -> LOGIN_FAIL")
    void loginPasswordValidation() {
        // GIVEN
        userRepository.save(User.builder()
                .email("suyoung@naver.com")
                .password(passwordEncoder.encode("1234"))
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build());

        LoginRequestDto loginRequestDto = new LoginRequestDto("suyoung@naver.com","5678");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            authService.login(loginRequestDto,response); });
        String message = exception.getMessage();

        // THEN
        assertThat(message).isEqualTo("유저 정보가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("<6> reissue -> 성공")
    void reissueSuccess() {
        // GIVEN
        userRepository.save(User.builder()
                .email("suyoung@naver.com")
                .password(passwordEncoder.encode("1234"))
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build());
        LoginRequestDto loginRequestDto = new LoginRequestDto("suyoung@naver.com","1234");
        MockHttpServletResponse response = new MockHttpServletResponse();
        TokenResponseDto responseDto = authService.login(loginRequestDto,response);
        TokenRequestDto tokenRequestDto= new TokenRequestDto(responseDto.getAccessToken(),responseDto.getRefreshToken());

        // WHEN
        TokenResponseDto tokenResponseDto = authService.reissue(tokenRequestDto,response);

        // THEN
        assertThat(tokenResponseDto).isNotNull();
    }

    @Test
    @DisplayName("<7> reissue -> TOKEN_EXPIRED")
    void reissueTokenExpired() {
        // GIVEN
        userRepository.save(User.builder()
                .email("suyoung@naver.com")
                .password(passwordEncoder.encode("1234"))
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build());
        LoginRequestDto loginRequestDto = new LoginRequestDto("suyoung@naver.com","1234");
        MockHttpServletResponse response = new MockHttpServletResponse();
        TokenResponseDto responseDto = authService.login(loginRequestDto,response);
        TokenRequestDto tokenRequestDto= new TokenRequestDto(responseDto.getAccessToken(),"Expired refreshToken");

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            authService.reissue(tokenRequestDto,response); });
        String message = exception.getMessage();

        // THEN
        assertThat(message).isEqualTo("토큰이 유효하지 않습니다.");
    }


}