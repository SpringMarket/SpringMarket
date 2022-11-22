package product.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import product.config.jwt.JwtAccessDeniedHandler;
import product.config.jwt.JwtAuthenticationEntryPoint;
import product.config.jwt.TokenProvider;
import product.config.log.AccessLogFilter;
import product.dto.user.SignUpRequestDto;
import product.service.user.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
//@Import(TestConfig.class)
//@WebAppConfiguration
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
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
                "    \"email\": \"suyoung1@naver.com\"," +
                "    \"password\":\"1234\"," +
                "    \"age\":\"20대\"" +
                "}";

        //String jsonContent = mapper.writeValueAsString(content);

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
    void login() {
        assertThat(1).isEqualTo(1);
    }

}
