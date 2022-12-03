package product.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import product.entity.user.Authority;
import product.entity.user.User;
import product.repository.user.UserRepository;
import product.service.order.OrderService;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WebAppConfiguration
class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;

    private String accessToken(String email){
        long now = (new Date()).getTime();
        String secretKey = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 60 * 24);
        return Jwts.builder()
                .setSubject(email)       // payload "sub": "name"
                .claim("auth", "ROLE_USER")        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();
    }
    @Test
    @DisplayName("주문")
    void order() throws Exception {
        String content = "{" +
                "\"orderNum\" : 3," +
                " \"productId\" : 200" +
                "}";

        mvc.perform(post("/api/order")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .header("Authorization","Bearer "+accessToken("suyoung@naver.com")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 리스트 조회")
    void orderList() throws Exception {
        User user = User.builder()
                .email("suyoung@naver.com")
                .password("1234")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        mvc.perform(get("/api/order/list")
                        .header("Authorization","Bearer "+accessToken("suyoung@naver.com")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 취소")
    void orderCancel() throws Exception {
        User user = User.builder()
                .email("suyoung@naver.com")
                .password("1234")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        mvc.perform(patch("/api/order/20")
                        .header("Authorization","Bearer "+accessToken("suyoung@naver.com")))
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