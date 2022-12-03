package product.service.cart;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import product.MysqlTestContainer;
import product.RedisTestContainer;
import product.dto.order.OrderRequestDto;
import product.dto.product.ProductDetailResponseDto;
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.exception.RequestException;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceTest extends RedisTestContainer {

    @Autowired
    private CartRedisService cartRedisService;
    @Autowired
    private RedisTemplate<String, List<Long>> redisTemplate;
    @Autowired
    private CartService cartService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductInfoRepository productInfoRepository;

    @BeforeAll
    void setProduct(){

        ProductInfo productInfo = ProductInfo.builder()
                .productInfoId(1L)
                .ten(10L)
                .twenty(20L)
                .thirty(30L)
                .over_forty(40L)
                .build();

        Product product_1 = Product.builder()
                .title("Test_1")
                .content("Test_1")
                .photo("Test_1")
                .price(10000L)
                .stock(10L)
                .view(3)
                .createdTime(LocalDateTime.now())
                .categoryId(1L)
                .productInfo(productInfo)
                .build();

        Product product_2 = Product.builder()
                .title("Test_2")
                .content("Test_2")
                .photo("Test_2")
                .price(10000L)
                .stock(10L)
                .view(3)
                .createdTime(LocalDateTime.now())
                .categoryId(1L)
                .productInfo(productInfo)
                .build();

        productInfoRepository.save(productInfo);
        productRepository.save(product_1);
        productRepository.save(product_2);

    }
    @AfterAll
    void flushallAfter() {
        Objects.requireNonNull(redisTemplate.keys("*"))
                .forEach(k-> {redisTemplate.delete(k);
                });
    }

    @Test
    @DisplayName("<1> 카트에 상품 추가 -> Default")
    void addCart() {

        // GIVEN
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        String key = "cart::Test::1";
        Long productId_1 = 1L;
        Long productId_2 = 2L;

        // WHEN
        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);
        List<Long> list = values.get("cart::Test::1");


        // THEN
        assertEquals(list.get(0), 1);
        assertEquals(list.get(1), 2);
    }

    @Test
    @DisplayName("<2> 카트에 상품 추가 -> Already Exist Data")
    void addCartAlreadyExistData(){
        // GIVEN
        String key = "cart::Test::2";
        Long productId_1 = 1L;

        // WHEN
        cartRedisService.addCart(key, productId_1);

        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartRedisService.addCart(key, productId_1); });
        String message = exception.getMessage();

        // THEN
        assertEquals("이미 존재하는 데이터입니다.", message);
    }

    @Test
    @WithMockUser(username = "Test::3")
    @DisplayName("<3> 카트에 상품 추가 -> Not Exist Data")
    void addCartNotExistData(){
        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long productId_1 = -1L;

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartService.addCart(productId_1, authentication); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);

    }



    @Test
    @DisplayName("<4> 카트 상품 삭제 -> Default")
    void deleteCart() {
        // GIVEN
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        String key = "cart::Test::4";
        Long productId_1 = 1L;

        // WHEN
        cartRedisService.addCart(key, productId_1);
        List<Long> list1 = values.get("cart::Test::4");

        cartRedisService.deleteCart(key, productId_1);
        List<Long> list2 = values.get("cart::Test::4");

        // THEN
        assertEquals(list1.get(0), 1L);
        assertEquals(list2.size(), 0);
    }


    @Test
    @DisplayName("<5> 카트 상품 삭제 -> Not Exist Redis Key")
    void deleteCartNotExistKey(){
        // GIVEN
        String key = "cart::Test::5";
        Long productId_1 = 1L;

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartRedisService.deleteCart(key, productId_1); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 키를 찾을 수 없습니다.", message);
    }

    @Test
    @WithMockUser(username = "Test::6")
    @DisplayName("<6> 카트 상품 삭제 -> Not Exist Data")
    void deleteCartNotExistData(){

        // GIVEN
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        String key = "cart::Test::6";
        Long productId_1 = 1L;
        Long productId_2 = 2L;
        cartRedisService.addCart(key, productId_1);

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartRedisService.deleteCart(key, productId_2); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);

    }



    @Test
    @DisplayName("<7> 카트 조회 -> Default")
    void showCart() {

        // GIVEN
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        String key = "cart::Test::7";
        Long productId_1 = 1L;
        Long productId_2 = 2L;

        // WHEN
        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);
        List<Long> list1 = values.get("cart::Test::7");

        List<Long> list2 = cartRedisService.cartList(key);

        // THEN
        assertEquals(list1, list2);
    }

    @Test
    @DisplayName("<8> 카트 조회 -> Not Exist Redis Key")
    void showCartNotExistKey(){
        // GIVEN
        String key = "cart::Test::8";

        // WHEN
        List<Long> list = cartRedisService.cartList(key);

        // THEN
        assertEquals(list.size(), 0);
    }



    @Test
    @WithMockUser(username = "Test::9")
    @DisplayName("<9> 카트에 담긴 상품 주문 -> Default")
    void orderCart() {
        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String key = "cart::Test::9";
        Long productId_1 = 1L;
        Long productId_2 = 2L;

        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);

        OrderRequestDto order_1 = new OrderRequestDto(1L, 1L);
        OrderRequestDto order_2 = new OrderRequestDto(1L, 1L);

        List<OrderRequestDto> orderList = new ArrayList<>();

         orderList.add(order_1);
         orderList.add(order_2);


        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartService.orderCart(authentication, orderList); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);
    }

    @Test
    @WithMockUser(username = "Test::10")
    @DisplayName("<10> 카트에 담긴 상품 주문 -> 주문 요청이 존재하지 않음")
    void orderCartNotExistRequest() {
        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String key = "cart::Test::10";
        Long productId_1 = 1L;
        Long productId_2 = 2L;

        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);

        List<OrderRequestDto> orderList = new ArrayList<>();

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartService.orderCart(authentication, orderList); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);
    }

    @Test
    @WithMockUser(username = "Test::11")
    @DisplayName("<11> 카트에 담긴 상품 주문 -> 카트에 든 상품과 요청이 일치하지 않음")
    void orderCartNotMatchCart() {
        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String key = "cart::Test::11";
        Long productId_1 = 1L;
        Long productId_2 = 2L;

        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);

        OrderRequestDto order_1 = new OrderRequestDto(3L, 1L);
        OrderRequestDto order_2 = new OrderRequestDto(4L, 1L);

        List<OrderRequestDto> orderList = new ArrayList<>();

        orderList.add(order_1);
        orderList.add(order_2);


        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartService.orderCart(authentication, orderList); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);
    }
}