package product.controller.product;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import product.config.jwt.JwtAccessDeniedHandler;
import product.config.jwt.JwtAuthenticationEntryPoint;
import product.config.jwt.TokenProvider;
import product.config.log.AccessLogFilter;
import product.service.product.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductControllerTest {
    @Autowired
    private MockMvc mvc;
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
    @DisplayName("RankingList")
    void RankingList() throws Exception {
        mvc.perform(get("/api/rank/list/1/1"))
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
        mvc.perform(get("/api/rank/list/1/1"));
        // then
        verify(productService,times(1)).getRankingList(1L,1L);
        //assertThat(productService.getRankingList(1L)).isNotNull();
    }

    @Test
    @DisplayName("???????????????")
    void findAllProduct() throws Exception {
        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{}}"));
    }

    @Test
    @DisplayName("??????????????? param")
    void findAllProductParam() throws Exception {
        mvc.perform(get("/api/products?category=??????&stock=1&maxPrice=100000&keyword=??????&minPrice=50000&sorting=20???&page=1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{}}"));
    }

    @Test
    @DisplayName("??????????????? ??????")
    void findAllProductLog() throws Exception {
        // given
        Logger log = (Logger) LoggerFactory.getLogger("ACCESS");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);

        // when
        mvc.perform(get("/api/products?categoryId=1&stock=1&maxPrice=100000&minPrice=50000&sorting=20???&page=1"));

        List<ILoggingEvent> testLogs = listAppender.list;
        Level level = testLogs.get(0).getLevel();
        String message = testLogs.get(0).getFormattedMessage();

        // then
        assertThat(level).isEqualTo(Level.INFO);
        assertThat(message).isEqualTo("categoryId: 1 stock: 1 minPrice: 50000 maxPrice: 100000 keyword: null sorting: 20???");
    }

    @Test
    @DisplayName("??????????????? verify")
    void findAllProductVerify() throws Exception {
        Pageable pageable = PageRequest.of(0,15);
        Long categoryId = 1L;
        String stock = "1";
        Long minPrice = 50000L;
        Long maxPrice = 100000L;
        String keyword = "??????";
        String sorting = "?????????";
        mvc.perform(get("/api/products" +
                        "?categoryId={categoryId}" +
                        "&stock={stock}" +
                        "&minPrice={minPrice}" +
                        "&maxPrice={maxPrice}" +
                        "&keyword={keyword}" +
                        "&sorting={sorting}",
                categoryId,stock,minPrice,maxPrice,keyword,sorting));

        verify(productService,times(1)).findAllProduct(pageable,categoryId,stock,minPrice,maxPrice,keyword,sorting);
    }

    @Test
    @DisplayName("???????????????")
    void findProduct() throws Exception {
        mvc.perform(get("/api/products/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{}}"));
    }

    @Test
    @DisplayName("??????????????? ??????")
    void findProductLog() throws Exception {

        // given
        Logger log = (Logger) LoggerFactory.getLogger("ACCESS");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);

        // when
        mvc.perform(get("/api/products/{id}",1L));

        List<ILoggingEvent> testLogs = listAppender.list;
        Level level = testLogs.get(0).getLevel();
        String message = testLogs.get(0).getFormattedMessage();

        // then
        assertThat(level).isEqualTo(Level.INFO);
        assertThat(message).isEqualTo("1??? ?????? ??????");
    }

    @Test
    @DisplayName("??????????????? verify")
    void findProductVerify() throws Exception {
        // when
        mvc.perform(get("/api/products/{id}",1L));
        // then
        verify(productService,times(1)).findProduct(1L);
    }

    @Test
    @DisplayName("??????????????? ????????? ??????")
    void keywordSearch() throws Exception {
        mvc.perform(get("/api/products/keyword?keyword=??????"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"result\":true," +
                        "\"httpStatus\":\"OK\"," +
                        "\"check\":{}}"));
    }

}
