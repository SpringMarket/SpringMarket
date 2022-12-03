package product.service.order;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import product.dto.order.MyPageResponseDto;
import product.entity.order.Order;
import product.entity.product.Category;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @BeforeAll
    @DisplayName("<Before> 상품 초기화")
    void setProduct(){
        Category category = Category.builder() // 카테고리는 인덱스로 가져오기
                .categoryId(1L)
                .category("Test")
                .build();

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

        categoryRepository.save(category);
        productInfoRepository.save(productInfo);
        productRepository.save(product_1);
        productRepository.save(product_2);
    }

    @Test
    @WithMockUser(username = "Order::1")
    @DisplayName("<1> 상품 주문 -> Default")
    void orderProduct() {

        // GIVEN
        User user = User.builder()
                .email("Order::1")
                .password("password")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 1L;
        Long orderNum = 1L;

        // WHEN
        orderService.orderProduct(productId, orderNum, authentication);
        Product product = productRepository.findByProductId(1L);

        // THEN
        assertThat(product.getStock()).isEqualTo(9L);
    }

    @Test
    @WithMockUser(username = "Order::2")
    @DisplayName("<2> 상품 주문 -> Not Exist Data")
    void orderProductNotExistData() {

        // GIVEN
        User user = User.builder()
                .email("Order::2")
                .password("password")
                .age("20대")
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
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);
    }

    @Test
    @WithMockUser(username = "Order::3")
    @DisplayName("<3> 상품 주문 -> Out Of Stock")
    void orderProductOutOfStock() {
        // GIVEN
        User user = User.builder()
                .email("Order::3")
                .password("password")
                .age("20대")
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
        assertEquals("재고가 부족합니다.", message);
    }

    @Test
    @WithMockUser(username = "Order::4")
    @DisplayName("<4> 주문 목록 조회 -> Default")
    void myPage() {
        // GIVEN
        User user = User.builder()
                .email("Order::4")
                .password("password")
                .age("20대")
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
    @DisplayName("<5> 주문 취소 -> Default")
    void orderCancel() {
        // GIVEN
        User user = User.builder()
                .email("Order::5")
                .password("password")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long productId = 1L;
        Product product = productRepository.findByProductId(productId);
        Long orderNum = 1L;

        Order order = new Order(orderNum, "배송중", product, user, 99L);

        orderRepository.save(order);
        Order order1 = orderRepository.findByOrderId(99L);
        System.out.println(order1);

        orderService.orderProduct(productId, orderNum, authentication);
        Product product_1 = productRepository.findByProductId(1L);
        Long stock_1 = product_1.getStock();

        // WHEN
        orderService.cancel(authentication, 2L);
        Product product_2 = productRepository.findByProductId(1L);
        Long stock_2 = product_2.getStock();

        // THEN
        assertEquals(stock_1, 9L);
        assertEquals(stock_2, 10L);
    }

//    @Test
//    @DisplayName("주문 취소")
//    void cancel() {
//        // GIVEN
//        Pageable pageable = Pageable.ofSize(10);
//        Long orderNum = 3L; // 주문 수량 : 3
//
//        Product product = productRepository.findByProductId(1L); // 재고 : 10
//        User user = userRepository.findByUserId(1L);
//
//        product.getProductInfo().plusPreference(orderNum, user.getAge()); // 주문 메소드  // 재고 : 7, 상태 : 배송중
//        Order order = new Order(product, orderNum, user); // 주문 저장
//
//        Product productCheck = productRepository.findByProductId(order.getProduct().getProductId()); // 주문된 상품 정보
//
//        // WHEN
//        if (!user.equals(order.getUser()))   // 주문자 일치여부 판단
//            throw new RequestException(ACCESS_DENIED_EXCEPTION);
//
//
//        if (order.getOrderStatus().equals("배송완료"))  // 배송완료 시 취소 불가
//            throw new RequestException(ORDER_FINISH_EXCEPTION);
//
//
//        if (productCheck == null) // 주문이 제대로 됐는지 확인
//            throw new RequestException(ExceptionType.NOT_FOUND_EXCEPTION);
//
//        // -> 위 3개의 예외처리가 잘 실행되는지 테스트코드 작성해야합니다.
//
//        product.getProductInfo().minusPreference(order.getOrderNum(), user.getAge()); // 재고 상태 변경
//        order.cancel(); // 진행 상황 변경
//
//        // THEN
//        assertThat(product).isEqualTo(productCheck);
//        assertThat(product.getStock().getStock()).isEqualTo(10);
//    }
}
