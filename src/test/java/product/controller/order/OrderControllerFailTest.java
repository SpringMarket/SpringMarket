package product.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import product.repository.user.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerFailTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("주문")
    @WithMockUser(username = "sy")
    void order() throws Exception {

        String content = "{" +
                "\"orderNum\" : 3," +
                " \"productId\" : 200" +
                "}";

        mvc.perform(post("/api/order")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"result\":false,\"httpStatus\":\"NOT_FOUND\",\"check\":{\"msg\":\"요청하신 자료를 찾을 수 없습니다.\"}}"));
    }
}

