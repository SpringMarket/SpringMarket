/*
package product.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import product.service.product.ProductService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ProductService productService;
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
        mvc.perform(get("/api/warmup"))
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
        mvc.perform(get("/api/warmup"));

        // then
        verify(productService,times(1)).warmup();
    }

    @Test
    @DisplayName("warmupRank")
    void warmupRank() throws Exception {
        mvc.perform(get("/api/warmup/rank"))
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
        mvc.perform(get("/api/warmup/rank"));

        // then
        verify(productService,times(1)).warmupRank();
    }

    @Test
    @DisplayName("RankingList")
    void RankingList() throws Exception {
        mvc.perform(get("/api/rank/list/{categoryId}",1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{}}"));
    }

    @Test
    @DisplayName("RankingList verify")
    void RankingListVerify() throws Exception {
        // when
        mvc.perform(get("/api/rank/list/{categoryId}",1L));
        // then
        verify(productService,times(1)).getRankingList(1L);
    }

}
*/
