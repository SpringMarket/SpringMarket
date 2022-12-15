package product.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import product.service.order.OrderService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;


    /*private String accessToken(String email){
        long now = (new Date()).getTime();
        String secretKey = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 60 * 24);
        return Jwts.builder()
                .setSubject(email) // payload "sub": "name"
                .claim("auth", "ROLE_USER") // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn) // payload "exp": 1516239022 (예시)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS512) // header "alg": "HS512"
                .compact();
    }*/
    @Test
    @DisplayName("주문")
//    @WithMockUser(username = "suyoung@naver.com")
    void order() throws Exception {

        String content = "{" +
                "\"orderNum\" : 3," +
                " \"productId\" : 200" +
                "}";

        mvc.perform(post("/api/order")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 리스트 조회")
//    @WithMockUser(username = "suyoung@naver.com")
    void orderList() throws Exception {

        mvc.perform(get("/api/order/list"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 취소")
//    @WithMockUser(username = "suyoung@naver.com")
    void orderCancel() throws Exception {

        mvc.perform(patch("/api/order/20"))
                .andExpect(status().isOk());
    }


/* UsernamePasswordAuthenticationToken authenticationToken =
new UsernamePasswordAuthenticationToken("suyoung@naver.com","1234");
Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);*/
/*Authentication authentication = Mockito.mock(Authentication.class);
// Mockito.whens() for your authorization object
SecurityContext securityContext = Mockito.mock(SecurityContext.class);
Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
SecurityContextHolder.setContext(securityContext);*/
}
