package product.service.mypage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import product.dto.mypage.MyPageResponseDto;
import product.entity.product.Category;
import product.entity.product.Order;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.user.Authority;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.product.CategoryRepository;
import product.repository.product.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;
import product.service.product.TestConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static product.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static product.exception.ExceptionType.ORDER_FINISH_EXCEPTION;

@DataJpaTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@Import(TestConfig.class)
@Rollback
class MyPageServiceTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeAll
    void setUp() {

        Category category = Category.builder()
                .categoryId(1L)
                .category("Test")
                .build();

        categoryRepository.save(category);

        ProductInfo productInfo = ProductInfo.builder()
                // Default productInfoId = 1L
                .ten(10L)
                .twenty(20L)
                .thirty(30L)
                .forty(40L)
                .stock(10L)
                .view(10L)
                .build();

        productInfoRepository.save(productInfo);

        Product product = Product.builder()
                // Default productId = 1L
                .title("Test")
                .content("Test")
                .photo("Test")
                .price(10000L)
                .category(category)
                .productInfo(productInfo)
                .build();

        productRepository.save(product);

        User user = User.builder()
                .email("jeyun")
                .password("1234")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();

        userRepository.save(user);

    }

    @Test
    @DisplayName("주문 조회")
    void myPage() {
        // GIVEN
        Pageable pageable = Pageable.ofSize(10);
        Long orderNum = 3L; // 주문 수량 : 3

        Product product = productRepository.findByProductId(1L); // Stock : 10
        User user = userRepository.findByUserId(1L);

        product.getProductInfo().order(orderNum); // 주문 메소드

        Order order = new Order(product, orderNum, user);
        orderRepository.save(order);  // 주문 저장

        // WHEN
        Page<MyPageResponseDto> list = orderRepository.orderFilter(user,pageable);

        // THEN
        assertThat(list.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 취소")
    void cancel() {
        // GIVEN
        Pageable pageable = Pageable.ofSize(10);
        Long orderNum = 3L; // 주문 수량 : 3

        Product product = productRepository.findByProductId(1L); // 재고 : 10
        User user = userRepository.findByUserId(1L);

        product.getProductInfo().order(orderNum); // 주문 메소드  // 재고 : 7, 상태 : 배송중
        Order order = new Order(product, orderNum, user); // 주문 저장

        Product productCheck = productRepository.findByProductId(order.getProduct().getProductId()); // 주문된 상품 정보

        // WHEN
        if (!user.equals(order.getUser()))   // 주문자 일치여부 판단
            throw new RequestException(ACCESS_DENIED_EXCEPTION);


        if (order.getOrderStatus().equals("배송완료"))  // 배송완료 시 취소 불가
            throw new RequestException(ORDER_FINISH_EXCEPTION);


        if (productCheck == null) // 주문이 제대로 됐는지 확인
            throw new RequestException(ExceptionType.NOT_FOUND_EXCEPTION);

        // -> 위 3개의 예외처리가 잘 실행되는지 테스트코드 작성해야합니다.

        product.getProductInfo().cancel(order.getOrderNum()); // 재고 상태 변경
        order.cancel(); // 진행 상황 변경

        // THEN
        assertThat(product).isEqualTo(productCheck);
        assertThat(product.getProductInfo().getStock()).isEqualTo(10);
    }
}