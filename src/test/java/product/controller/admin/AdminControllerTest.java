package product.controller.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import product.config.jwt.JwtAccessDeniedHandler;
import product.config.jwt.JwtAuthenticationEntryPoint;
import product.config.jwt.TokenProvider;
import product.config.log.AccessLogFilter;
import product.service.admin.AdminService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AdminService adminService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @MockBean
    private AccessLogFilter accessLogFilter;



    @Test
    @DisplayName("warmup")
    void warmup() throws Exception {
        mvc.perform(get("/warmup"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{\"data\":\"SUCCESS ^__^ !!\"}}"));
    }

    @Test
    @DisplayName("warmup verify")
    void warmupVerify() throws Exception {
        // when
        mvc.perform(get("/warmup"));

        // then
        verify(adminService,times(1)).warmup();
    }

    @Test
    @DisplayName("warmupRank")
    void warmupRank() throws Exception {
        mvc.perform(get("/warmup/rank"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{\"data\":\"SUCCESS ^__^ !!\"}}"));
    }

    @Test
    @DisplayName("warmupRank verify")
    void warmupRankVerify() throws Exception {
        // when
        mvc.perform(get("/warmup/rank"));

        // then
        verify(adminService,times(1)).warmupRank();
    }

    @Test
    @DisplayName("warmupPipeLine")
    void warmupPipeLine() throws Exception {
        mvc.perform(get("/warmup/pipe/{categoryId}",1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{\"data\":\"SUCCESS ^__^ !!\"}}"));
    }

    @Test
    @DisplayName("warmupPipeLine verify")
    void warmupPipeLineVerify() throws Exception {
        // when
        mvc.perform(get("/warmup/pipe/{categoryId}",1L));

        // then
        verify(adminService,times(1)).warmupPipeLine(1L);
    }

    @Test
    @DisplayName("warmupRankingPipeLine")
    void warmupRankingPipeLine() throws Exception {
        mvc.perform(get("/warmup/rank/pipe/{categoryId}",1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{\"data\":\"SUCCESS ^__^ !!\"}}"));
    }

    @Test
    @DisplayName("warmupRankingPipeLine verify")
    void warmupRankingPipeLineVerify() throws Exception {
        // when
        mvc.perform(get("/warmup/rank/pipe/{categoryId}",1l));

        // then
        verify(adminService,times(1)).warmupRankingPipeLine(1L);
    }


}

