package product.service.cart;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import product.RedisTestContainer;
import product.dto.order.OrderRequestDto;
import product.dto.product.ProductMainResponseDto;
import product.entity.order.Orders;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.user.Authority;
import product.entity.user.User;
import product.exception.RequestException;
import product.repository.order.OrderRepository;
import product.repository.product.CategoryRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceTest extends RedisTestContainer {

    @Autowired
    private CartRedisService cartRedisService;

    @Autowired
    private RedisTemplate<String, List<Long>> redisTemplate;
    @Autowired
    private CartService cartService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;


    @BeforeAll
    @DisplayName("<Before> 상품 초기화")
    void setProduct(){

        ProductInfo productInfo = ProductInfo.builder()
                .productInfoId(1L)
                .ten(10L)
                .twenty(20L)
                .thirty(30L)
                .over_forty(40L)
                .build();

        Product product_1 = Product.builder()
                .productId(1L)
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
                .productId(2L)
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
    @WithMockUser(username = "Cart::1")
    @DisplayName("<1> 카트에 상품 추가 -> Default")
    void addCart() {
        // GIVEN
        User user = User.builder()
                .email("Cart::1")
                .password("password")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();

        Long productId_1 = 1L;
        Long productId_2 = 2L;

        // WHEN
        cartService.addCart(productId_1, authentication);
        cartService.addCart(productId_2, authentication);
        List<Long> list = values.get("cart::Cart::1");


        // THEN
        assertEquals(list.get(0), 1);
        assertEquals(list.get(1), 2);
    }

    @Test
    @DisplayName("<2> 카트에 상품 추가 -> Over Exist")
    void addCartOverExist() {
        // GIVEN
        String key = "cart::Cart::2";
        Long productId_1 = 1L;
        Long productId_2 = 2L;
        Long productId_3 = 3L;
        Long productId_4 = 4L;
        Long productId_5 = 5L;
        Long productId_6 = 6L;
        Long productId_7 = 7L;
        Long productId_8 = 8L;
        Long productId_9 = 9L;
        Long productId_10 = 10L;
        Long productId_11 = 11L;

        // WHEN
        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);
        cartRedisService.addCart(key, productId_3);
        cartRedisService.addCart(key, productId_4);
        cartRedisService.addCart(key, productId_5);
        cartRedisService.addCart(key, productId_6);
        cartRedisService.addCart(key, productId_7);
        cartRedisService.addCart(key, productId_8);
        cartRedisService.addCart(key, productId_9);
        cartRedisService.addCart(key, productId_10);

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartRedisService.addCart(key, productId_11); });
        String message = exception.getMessage();

        // THEN
        assertEquals("최대 요청 개수를 초과하였습니다.", message);
    }

    @Test
    @DisplayName("<3> 카트에 상품 추가 -> Already Exist Data")
    void addCartAlreadyExistData(){
        // GIVEN
        String key = "cart::Cart::3";
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
    @WithMockUser(username = "Cart::4")
    @DisplayName("<4> 카트에 상품 추가 -> Not Exist Data")
    void addCartNotExistData(){
        // GIVEN
        User user = User.builder()
                .email("Cart::4")
                .password("password")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
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
    @WithMockUser(username = "Cart::5")
    @DisplayName("<5> 카트 상품 삭제 -> Default")
    void deleteCart() {
        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();

        List<Long> longList = new ArrayList<>();
        longList.add(1L);

        values.set("cart::Cart::5", longList);
        List<Long> list1 = values.get("cart::Cart::5");



        // WHEN
//        cartService.addCart(productId_1, authentication);
//        List<Long> list1 = values.get("cart::Cart::5");
        Long productId_1 = 1L;
        cartService.deleteCart(productId_1, authentication);
        List<Long> list2 = values.get("cart::Cart::5");

        // THEN
        assertEquals(list1.size(), 1);
        assertEquals(list2.size(), 0);
    }


    @Test
    @DisplayName("<6> 카트 상품 삭제 -> Not Exist Redis Key")
    void deleteCartNotExistKey(){
        // GIVEN
        String key = "cart::Cart::6";
        Long productId_1 = 1L;

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            cartRedisService.deleteCart(key, productId_1); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 키를 찾을 수 없습니다.", message);
    }

    @Test
    @WithMockUser(username = "Cart::7")
    @DisplayName("<7> 카트 상품 삭제 -> Not Exist Data")
    void deleteCartNotExistData(){

        // GIVEN
        String key = "cart::Cart::7";
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
    @WithMockUser(username = "Cart::8")
    @DisplayName("<8> 카트 조회 -> Default")
    void showCart() {

        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        Pageable pageable = Pageable.ofSize(10);
        String key = "cart::Cart::8";
        Long productId_1 = 1L;
        Long productId_2 = 2L;

        // WHEN
        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);
        List<Long> list1 = values.get("cart::Cart::8");

        List<ProductMainResponseDto> list2 = cartService.showCart(authentication, pageable);

        // THEN
        assertEquals(list2.get(0).getProductId(), 1L);
        assertEquals(list2.get(1).getProductId(), 2L);
    }

    @Test
    @DisplayName("<9> 카트 조회 -> Not Exist Redis Key")
    void showCartNotExistKey(){
        // GIVEN
        String key = "cart::Cart::9";

        // WHEN
        List<Long> list = cartRedisService.cartList(key);

        // THEN
        assertEquals(list.size(), 0);
    }



    @Test
    @WithMockUser(username = "Cart::10")
    @DisplayName("<10> 카트에 담긴 상품 주문 -> Default")
    void orderCart() {
        // GIVEN
        User user = User.builder()
                .email("Cart::10")
                .password("password")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String key = "cart::Cart::10";
        Long productId_1 = 1L;
        Long productId_2 = 2L;

        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);

        OrderRequestDto order_1 = new OrderRequestDto(1L, 1L);
        OrderRequestDto order_2 = new OrderRequestDto(2L, 1L);

        List<OrderRequestDto> orderList = new ArrayList<>();

         orderList.add(order_1);
         orderList.add(order_2);


        // WHEN
        cartService.orderCart(authentication, orderList);

        Orders order = orderRepository.findByOrderId(1L);
        Orders orderOrder = orderRepository.findByOrderId(2L);

        // THEN
        assertEquals(order.getProduct().getTitle(), "Test_1");
        assertEquals(orderOrder.getProduct().getTitle(), "Test_2");
    }

    @Test
    @WithMockUser(username = "Cart::11")
    @DisplayName("<11> 카트에 담긴 상품 주문 -> 주문 요청이 존재하지 않음")
    void orderCartNotExistRequest() {
        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String key = "cart::Cart::11";
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
        assertEquals("주문 리스트에 담긴 상품이 없습니다.", message);
    }

    @Test
    @WithMockUser(username = "Cart::12")
    @DisplayName("<12> 카트에 담긴 상품 주문 -> 카트에 든 상품과 요청이 일치하지 않음")
    void orderCartNotMatchCart() {
        // GIVEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String key = "cart::Cart::12";
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