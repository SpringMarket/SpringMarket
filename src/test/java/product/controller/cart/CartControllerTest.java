package product.controller.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import product.service.cart.CartService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CartService cartService;


    @Test
    @DisplayName("카트 조회")
    void showCart() throws Exception {
        mvc.perform(get("/api/cart/list"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카트에 추가")
    void addCart() throws Exception {
        mvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카트에서 삭제")
    void deleteCart() throws Exception {
        mvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카트에서 주문")
    void orderCart() throws Exception {
        String content = "[\n" +
                "        {\"orderNum\" : 3,\n" +
                "    \"productId\" : 200},\n" +
                "    {\"orderNum\" : 1,\n" +
                "    \"productId\" : 100}\n" +
                "    ]";

        mvc.perform(post("/api/cart/order")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}