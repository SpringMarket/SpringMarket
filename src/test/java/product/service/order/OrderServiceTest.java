package product.service.order;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import product.dto.order.MyPageResponseDto;
import product.entity.product.*;
import product.entity.order.Order;
import product.entity.user.Authority;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.product.*;
import product.repository.order.OrderRepository;
import product.repository.user.UserRepository;
import product.service.product.TestConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static product.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static product.exception.ExceptionType.ORDER_FINISH_EXCEPTION;

@DataJpaTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@Import(TestConfig.class)
@Rollback
class OrderServiceTest {

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
    @Autowired
    StockRepository stockRepository;
    @Autowired
    ViewRepository viewRepository;

    @BeforeAll
    void setUp(){

        Product product = Product.builder()
                .title("Test")
                .content("Test")
                .photo("Test")
                .price(10000L)
                .build();

        Category category = Category.builder() // 카테고리는 인덱스로 가져오기
                .categoryId(1L)
                .category("Test")
                .build();

        categoryRepository.save(category);

        ProductInfo productInfo = ProductInfo.builder()
                .productInfoId(1L)
                .ten(10L)
                .twenty(20L)
                .thirty(30L)
                .over_forty(40L)
                .build();

        productInfoRepository.save(productInfo);

        Stock stock = Stock.builder()
                .stock_id(1L)
                .stock(10L)
                .build();

        stockRepository.save(stock);

        View view = View.builder()
                .view_id(1L)
                .view(50L)
                .build();

        viewRepository.save(view);

        product.setCategory(category);
        product.setProductInfo(productInfo);
        product.setView(view);
        product.setStock(stock);

        productRepository.save(product);
    }

    @Test
    @DisplayName("주문 조회")
    void myPage() {
        // GIVEN
        Pageable pageable = Pageable.ofSize(10);
        Long orderNum = 3L; // 주문 수량 : 3

        Product product = productRepository.findByProductId(1L); // Stock : 10
        User user = userRepository.findByUserId(1L);

        product.getProductInfo().PlusPreference(orderNum, user.getAge()); // 주문 메소드

        Order order = new Order(product, orderNum, user);
        orderRepository.save(order);  // 주문 저장

        // WHEN
        Page<MyPageResponseDto> list = orderRepository.orderFilter(user,pageable);

        // THEN
        assertThat(list.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 주문")
    void orderProduct() {

        // GIVEN
        Product product = productRepository.findByProductId(1L); // Stock = 10
        User user = userRepository.findByUserId(1L);
        Long orderNum = 3L;

        // WHEN
        product.getProductInfo().PlusPreference(orderNum, user.getAge());
        product.getStock().order(orderNum);

        Order order = new Order(product, orderNum, user);

        // THEN
        assertThat(order.getProduct().getStock().getStock()).isEqualTo(7L);
    }

    @Test
    @DisplayName("주문 취소")
    void cancel() {
        // GIVEN
        Pageable pageable = Pageable.ofSize(10);
        Long orderNum = 3L; // 주문 수량 : 3

        Product product = productRepository.findByProductId(1L); // 재고 : 10
        User user = userRepository.findByUserId(1L);

        product.getProductInfo().PlusPreference(orderNum, user.getAge()); // 주문 메소드  // 재고 : 7, 상태 : 배송중
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

        product.getProductInfo().MinusPreference(order.getOrderNum(), user.getAge()); // 재고 상태 변경
        order.cancel(); // 진행 상황 변경

        // THEN
        assertThat(product).isEqualTo(productCheck);
        assertThat(product.getStock().getStock()).isEqualTo(10);
    }
}