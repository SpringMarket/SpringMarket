package product.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import product.MysqlTestContainer;
import product.entity.user.Authority;
import product.entity.user.User;
import product.repository.user.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(MockitoExtension.class)
class OrderControllerTest extends MysqlTestContainer {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;

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
    @WithMockUser(username = "suyoung@naver.com")
    void order() throws Exception {
        User user = User.builder()
                .email("suyoung@naver.com")
                .password("1234")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);

        String content = "{" +
                "\"orderNum\" : 3," +
                " \"productId\" : 200" +
                "}";

        mvc.perform(post("/api/order")
                        .content(content)
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isForbidden());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        mvc.perform(get("/api/order/list")
                        .with(authentication(authentication)))
                .andExpect(status().isForbidden());
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
        mvc.perform(patch("/api/order/20").with(anonymous()))
                .andExpect(status().isForbidden());
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
