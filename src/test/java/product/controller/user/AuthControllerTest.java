package product.controller.user;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import product.config.jwt.JwtAccessDeniedHandler;
import product.config.jwt.JwtAuthenticationEntryPoint;
import product.config.jwt.TokenProvider;
import product.config.log.AccessLogFilter;
import product.service.user.AuthService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
//@Import(TestConfig.class)
//@WebAppConfiguration
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    protected AuthService authService;
    @MockBean
    protected TokenProvider tokenProvider;
    @MockBean
    protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    protected JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @MockBean
    protected AccessLogFilter accessLogFilter;


    @Test
    @DisplayName("회원 가입 성공")
    void signup() throws Exception {

        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20대\"" +
                "}";
        /*SignUpRequestDto signUpRequestDto = new SignUpRequestDto("suyoung@naver.com","1234","20대");
        String jsonContent = mapper.writeValueAsString(signUpRequestDto);*/

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"" +
                        "}"));
    }
    @Test
    @DisplayName("회원 가입 로그")
    void signupLog() throws Exception {

        // given
        Logger log = (Logger) LoggerFactory.getLogger("ACCESS");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);

        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20대\"" +
                "}";

        // when
        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"));

        List<ILoggingEvent> testLogs = listAppender.list;
        Level level = testLogs.get(0).getLevel();
        String message = testLogs.get(0).getFormattedMessage();

        // then
        assertThat(level).isEqualTo(Level.INFO);
        assertThat(message).isEqualTo("suyoung@naver.com 님이 가입하셨습니다.");

    }

    @Test
    @DisplayName("회원 가입 이메일 형식 오류")
    void signupEmailForm() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \" \"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20대\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"이메일 형식만 가능합니다.\"}}"));
    }
    @Test
    @DisplayName("회원 가입 이메일 null")
    void signupEmailNull() throws Exception {
        // given
        String content = "{" +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20대\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"이메일을 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("회원 가입 이메일 empty")
    void signupEmailEmpty() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20대\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"이메일을 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("회원 가입 패스워드 null")
    void signupPasswordNull() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"age\":\"20대\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"비밀번호를 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("회원 가입 패스워드 empty")
    void signupPasswordEmpty() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"\"," +
                "    \"age\":\"20대\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"비밀번호를 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("회원 가입 패스워드 blank")
    void signupPasswordBlank() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\" \"," +
                "    \"age\":\"20대\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"비밀번호를 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("회원 가입 연령대 null")
    void signupAgeNull() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"연령대를 선택해주세요.\"}}"));
    }

    @Test
    @DisplayName("회원 가입 연령대 다른 값")
    void signupAgeError() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"40대\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"연령대를 다시 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        // given
        /*LoginRequestDto loginRequestDto = new LoginRequestDto("suyoung@naver.com","1234");
        String jsonContent = mapper.writeValueAsString(loginRequestDto);*/

        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"" +
                "}";
        // then
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공 로그")
    void loginLog() throws Exception {

        // given
        Logger log = (Logger) LoggerFactory.getLogger("ACCESS");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);

        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"" +
                "}";

        // when
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"));

        List<ILoggingEvent> testLogs = listAppender.list;
        Level level = testLogs.get(0).getLevel();
        String message = testLogs.get(0).getFormattedMessage();

        // then
        assertThat(level).isEqualTo(Level.INFO);
        assertThat(message).isEqualTo("suyoung@naver.com 님이 로그인하셨습니다.");
    }

    @Test
    @DisplayName("로그인 이메일 형식오류")
    void loginEmailError() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"@naver\"," +
                "    \"password\":\"1234\"" +
                "}";
        // then
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"이메일 형식만 가능합니다.\"}}"));
    }

    @Test
    @DisplayName("로그인 이메일 null")
    void loginEmailNull() throws Exception {
        // given
        String content = "{" +
                "    \"password\":\"1234\"" +
                "}";
        // then
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"이메일을 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("로그인 이메일 empty")
    void loginEmailEmpty() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"\"," +
                "    \"password\":\"1234\"" +
                "}";
        // then
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"이메일을 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("로그인 패스워드 null")
    void loginPasswordNull() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"" +
                "}";
        // then
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"비밀번호를 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("로그인 패스워드 empty")
    void loginPasswordEmpty() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"\"" +
                "}";
        // then
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"비밀번호를 입력해주세요.\"}}"));
    }

    @Test
    @DisplayName("로그인 패스워드 blank")
    void loginPasswordBlank() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"  \"" +
                "}";
        // then
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"비밀번호를 입력해주세요.\"}}"));
    }

}
