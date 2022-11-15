package product.service.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductResponseDetailDto;
import product.entity.product.*;
import product.entity.order.Order;
import product.entity.user.Authority;
import product.entity.user.User;
import product.repository.product.*;
import product.repository.order.OrderRepository;
import product.repository.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @BeforeAll 어노테이션 사용 시 @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// EntityManager 의 .persist 사용 오류 ->  < 해결 필요 >
// + Repository 에 영속화 시키는 형식으로 변환

@DataJpaTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@Import(TestConfig.class)
@Rollback
class ProductServiceTest {

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
                .over_forty(40L)
                .build();

        productInfoRepository.save(productInfo);

        Stock stock = Stock.builder()
                // Default stockId = 1L
                .stock(10L)
                .build();

        stockRepository.save(stock);

        View view = View.builder()
                // Default viewId = 1L
                .view(50L)
                .build();

        viewRepository.save(view);

        Product product = Product.builder()
                // Default productId = 1L
                .title("Test")
                .content("Test")
                .photo("Test")
                .price(10000L)
                .category(category)
                .productInfo(productInfo)
                .stock(stock)
                .view(view)
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
    @DisplayName("상품 캐싱")
    void warmup() {

        //WHEN
        List<Product> warmupProduct = productRepository.warmup(1L);

        // THEN
        assertThat(warmupProduct.size()).isEqualTo(1);
    }


    @Test
    @DisplayName("상품 전체 조회")
    void findAllProduct() {

        // GIVEN
        Pageable pageable = Pageable.ofSize(10);
        String category = null;
        Boolean stock = true;
        Long minPrice = null;
        Long maxPrice = null;
        String keyword = null;
        String sort = null;

        // WHEN
        Page<ProductResponseDetailDto> list = productRepository.mainFilter(pageable, category, stock, minPrice, maxPrice, keyword, sort);

        // THEN
        assertThat(list.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 전체 조회 - Null")
    void findAllProductNull() {

        // GIVEN
        Pageable pageable = Pageable.ofSize(10);
        String category = null;
        Boolean stock = true; // Default 처리 필요
        Long minPrice = 10L;
        Long maxPrice = 1000000L;
        String keyword = "Test";
        String sort = "조회순";

        // WHEN
        Page<ProductResponseDetailDto> list = productRepository.mainFilter(pageable, category, stock, minPrice, maxPrice, keyword, sort);

        // THEN
        assertThat(list.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 상세 조회")
    void findProduct() {

        //WHEN
        Product product1 = productRepository.detail(1L);
        Product product2 = productRepository.findByProductId(1L);

        // THEN
        assertThat(product1).isEqualTo(product2);
    }
}