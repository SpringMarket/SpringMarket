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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import product.config.jwt.JwtAccessDeniedHandler;
import product.config.jwt.JwtAuthenticationEntryPoint;
import product.config.jwt.TokenProvider;
import product.config.log.AccessLogFilter;
import product.dto.user.LoginRequestDto;
import product.dto.user.SignUpRequestDto;
import product.service.user.AuthService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @MockBean
    private AccessLogFilter accessLogFilter;


    @Test
    @DisplayName("?????? ?????? ??????")
    void signup() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20???\"" +
                "}";

        // when
        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
        // then
                .andExpect(status().isOk())
                .andExpect(content().string("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"" +
                        "}"));
    }
    @Test
    @DisplayName("?????? ?????? ??????")
    void signupLog() throws Exception {

        // given
        Logger log = (Logger) LoggerFactory.getLogger("ACCESS");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);

        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20???\"" +
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
        assertThat(message).isEqualTo("suyoung@naver.com ?????? ?????????????????????.");
    }

    @Test
    @DisplayName("?????? ?????? verify")
    void signupVerify() throws Exception {

        // given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("suyoung@naver.com","1234","20???");
        String content = mapper.writeValueAsString(signUpRequestDto);
        // when
        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"));
        // then
        verify(authService,times(1)).signup(signUpRequestDto);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ??????")
    void signupEmailForm() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \" \"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20???\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"????????? ????????? ???????????????.\"}}"));
    }
    @Test
    @DisplayName("?????? ?????? ????????? null")
    void signupEmailNull() throws Exception {
        // given
        String content = "{" +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20???\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"???????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("?????? ?????? ????????? empty")
    void signupEmailEmpty() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20???\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"???????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("?????? ?????? ???????????? null")
    void signupPasswordNull() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"age\":\"20???\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"??????????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("?????? ?????? ???????????? empty")
    void signupPasswordEmpty() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"\"," +
                "    \"age\":\"20???\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"??????????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("?????? ?????? ???????????? blank")
    void signupPasswordBlank() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\" \"," +
                "    \"age\":\"20???\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"??????????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("?????? ?????? ????????? null")
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
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"???????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ???")
    void signupAgeError() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"40???\"" +
                "}";

        mvc.perform(post("/api/signup")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"???????????? ?????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("????????? ??????")
    void login() throws Exception {
        // given
        String content = "{" +
                "    \"email\": \"suyoung@naver.com\"," +
                "    \"password\":\"1234\"" +
                "}";
        // when
        mvc.perform(post("/api/login")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
        // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("????????? ?????? ??????")
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
        assertThat(message).isEqualTo("suyoung@naver.com ?????? ????????????????????????.");
    }

    @Test
    @DisplayName("????????? verify")
    void loginVerify() throws Exception {

        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto("suyoung@naver.com","1234");
        String content = mapper.writeValueAsString(loginRequestDto);
        // when
        MvcResult mvcResult =  mvc.perform(post("/api/login")
                .content(content)
                .contentType("application/json")
                .characterEncoding("UTF-8"))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        // then
        verify(authService,times(1)).login(loginRequestDto, response);
    }


    @Test
    @DisplayName("????????? ????????? ????????????")
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
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"????????? ????????? ???????????????.\"}}"));
    }

    @Test
    @DisplayName("????????? ????????? null")
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
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"???????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("????????? ????????? empty")
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
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"???????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("????????? ???????????? null")
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
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"??????????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("????????? ???????????? empty")
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
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"??????????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("????????? ???????????? blank")
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
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"BAD_REQUEST\",\"check\":{\"msg\":\"??????????????? ??????????????????.\"}}"));
    }

    @Test
    @DisplayName("reissue")
    void reissue() throws Exception {
        // given
        String content = "{" +
                "    \"accessToken\": \"Bearer accessToken\"," +
                "    \"refreshToken\":\" Bearer refreshToken \"" +
                "}";
        // then
        mvc.perform(post("/api/reissue")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

}
