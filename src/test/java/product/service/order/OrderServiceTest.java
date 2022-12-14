package product.service.order;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import product.MysqlTestContainer;
import product.config.rds.DbConfig;
import product.dto.order.MyPageResponseDto;
import product.entity.order.Orders;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.user.Authority;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.order.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderServiceTest extends MysqlTestContainer{

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager entityManager;


    @BeforeAll
    @DisplayName("<Before> ?????? ?????????")
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

        Product product_3 = Product.builder()
                .productId(3L)
                .title("Test_3")
                .content("Test_3")
                .photo("Test_3")
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
        productRepository.save(product_3);
    }

    @AfterEach
    public void teardown() {
        this.orderRepository.deleteAll();
        this.entityManager
                .createNativeQuery("TRUNCATE TABLE orders")
                .executeUpdate();
    }

    @Test
    @WithMockUser(username = "Order::1")
    @DisplayName("<1> ?????? ?????? -> Default")
    void orderProduct() {

        // GIVEN
        User user = User.builder()
                .email("Order::1")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 2L;
        Long orderNum = 1L;

        // WHEN
        orderService.orderProduct(productId, orderNum, authentication);
        Product product = productRepository.findByProductId(2L);

        // THEN
        assertThat(product.getStock()).isEqualTo(9L);
    }

    @Test
    @WithMockUser(username = "Order::2")
    @DisplayName("<2> ?????? ?????? -> Not Exist Data")
    void orderProductNotExistData() {

        // GIVEN
        User user = User.builder()
                .email("Order::2")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 999L;
        Long orderNum = 1L;

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            orderService.orderProduct(productId, orderNum, authentication); });
        String message = exception.getMessage();

        // THEN
        assertEquals("???????????? ????????? ?????? ??? ????????????.", message);
    }

    @Test
    @WithMockUser(username = "Order::3")
    @DisplayName("<3> ?????? ?????? -> Out Of Stock")
    void orderProductOutOfStock() {
        // GIVEN
        User user = User.builder()
                .email("Order::3")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 1L;
        Long orderNum = 999L;

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            orderService.orderProduct(productId, orderNum, authentication); });
        String message = exception.getMessage();

        // THEN
        assertEquals("????????? ???????????????.", message);
    }

    @Test
    @WithMockUser(username = "Order::4")
    @DisplayName("<4> ?????? ?????? ?????? -> Default")
    void myPage() {
        // GIVEN
        User user = User.builder()
                .email("Order::4")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Pageable pageable = Pageable.ofSize(10);


        // WHEN
        Page<MyPageResponseDto> page = orderService.myPage(pageable, authentication);

        // THEN
        assertThat(page.getTotalElements()).isEqualTo(0);
    }


    @Test
    @WithMockUser(username = "Order::5")
    @DisplayName("<5> ?????? ?????? -> Default")
    void orderCancel() {
        // GIVEN
        User user = User.builder()
                .email("Order::5")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 3L;
        Long orderNum = 1L;

        orderService.orderProduct(productId, orderNum, authentication);
        Product product_1 = productRepository.findByProductId(3L);
        Long stock_1 = product_1.getStock();

        // WHEN
        orderService.cancel(authentication, 1L);
        Product product_2 = productRepository.findByProductId(3L);
        Long stock_2 = product_2.getStock();

        // THEN
        assertEquals(stock_1, 9L);
        assertEquals(stock_2, 10L);
    }

    @Test
    @WithMockUser(username = "Order::6")
    @DisplayName("<6> ?????? ?????? -> Not Match User")
    void orderCancelNotMatchUser() {
        // GIVEN
        User user = User.builder()
                .email("Order::6")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user_check = User.builder()
                .email("Order::6::2")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user_check);

        Long productId = 1L;
        Long orderNum = 1L;

        this.entityManager
                .createNativeQuery("TRUNCATE TABLE orders")
                .executeUpdate();

        orderService.orderProduct(productId, orderNum, authentication);
        Orders order = orderRepository.findById(1L).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));
        System.out.println(order);

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
             orderService.checkCancelValid(user_check, order); });
        String message = exception.getMessage();

        // THEN
        assertEquals("????????? ??? ??????????????????.", message);
    }

    @Test
    @WithMockUser(username = "Order::7")
    @DisplayName("<7> ?????? ?????? -> Fail Status ????????????")
    void orderCancelFailStatus_1() {
        // GIVEN
        User user = User.builder()
                .email("Order::7")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 1L;
        Long orderNum = 1L;

        orderService.orderProduct(productId, orderNum, authentication);
        Orders order = orderRepository.findByOrderId(1L);
        order.setOrderStatus("????????????");

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            orderService.cancel(authentication, 1L); });
        String message = exception.getMessage();

        // THEN
        assertEquals("????????? ??????????????? ????????? ???????????????.", message);
    }

    @Test
    @WithMockUser(username = "Order::8")
    @DisplayName("<7> ?????? ?????? -> Fail Status ????????????")
    void orderCancelFailStatus_2() {
        // GIVEN
        User user = User.builder()
                .email("Order::8")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 1L;
        Long orderNum = 1L;

        orderService.orderProduct(productId, orderNum, authentication);
        Orders order = orderRepository.findByOrderId(1L);
        order.setOrderStatus("????????????");

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            orderService.cancel(authentication, 1L); });
        String message = exception.getMessage();

        // THEN
        assertEquals("????????? ??????????????? ????????? ???????????????.", message);
    }

    @Test
    @WithMockUser(username = "Order::9")
    @DisplayName("<7> ?????? ?????? -> Not Exist Order")
    void orderCancelNotExistOrder() {
        // GIVEN
        User user = User.builder()
                .email("Order::9")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 1L;
        Long orderNum = 1L;

        orderService.orderProduct(productId, orderNum, authentication);

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            orderService.cancel(authentication, 2L); });
        String message = exception.getMessage();

        // THEN
        assertEquals("???????????? ????????? ?????? ??? ????????????.", message);
    }

    @Test
    @WithMockUser(username = "Order::10")
    @DisplayName("<8> ?????? ?????? ?????? -> Access Denied")
    void myPageAccessDenied() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteAll();

        Pageable pageable = Pageable.ofSize(10);

        RequestException exception = assertThrows(RequestException.class, ()-> {
            orderService.myPage(pageable, authentication); });
        String message = exception.getMessage();

        // THEN
        assertEquals("????????? ??? ??????????????????.", message);
    }

    @Test
    @WithMockUser(username = "Order::11")
    @DisplayName("<9> ?????? ?????? -> Order Num Exception")
    void orderProductOrderNum() {

        // GIVEN
        User user = User.builder()
                .email("Order::11")
                .password("password")
                .age("20???")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 999L;
        Long orderNum = 0L;

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            orderService.orderProduct(productId, orderNum, authentication); });
        String message = exception.getMessage();

        // THEN
        assertEquals("?????? ????????? ??????????????????.", message);
    }
}

//    @Test
//    @DisplayName("?????? ??????")
//    void cancel() {
//        // GIVEN
//        Pageable pageable = Pageable.ofSize(10);
//        Long orderNum = 3L; // ?????? ?????? : 3
//
//        Product product = productRepository.findByProductId(1L); // ?????? : 10
//        User user = userRepository.findByUserId(1L);
//
//        product.getProductInfo().plusPreference(orderNum, user.getAge()); // ?????? ?????????  // ?????? : 7, ?????? : ?????????
//        Order order = new Order(product, orderNum, user); // ?????? ??????
//
//        Product productCheck = productRepository.findByProductId(order.getProduct().getProductId()); // ????????? ?????? ??????
//
//        // WHEN
//        if (!user.equals(order.getUser()))   // ????????? ???????????? ??????
//            throw new RequestException(ACCESS_DENIED_EXCEPTION);
//
//
//        if (order.getOrderStatus().equals("????????????"))  // ???????????? ??? ?????? ??????
//            throw new RequestException(ORDER_FINISH_EXCEPTION);
//
//
//        if (productCheck == null) // ????????? ????????? ????????? ??????
//            throw new RequestException(ExceptionType.NOT_FOUND_EXCEPTION);
//
//        // -> ??? 3?????? ??????????????? ??? ??????????????? ??????????????? ?????????????????????.
//
//        product.getProductInfo().minusPreference(order.getOrderNum(), user.getAge()); // ?????? ?????? ??????
//        order.cancel(); // ?????? ?????? ??????
//
//        // THEN
//        assertThat(product).isEqualTo(productCheck);
//        assertThat(product.getStock().getStock()).isEqualTo(10);
//    }
