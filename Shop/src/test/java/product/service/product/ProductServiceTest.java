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
import product.entity.product.Category;
import product.entity.product.Order;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.user.Authority;
import product.entity.user.User;
import product.repository.product.CategoryRepository;
import product.repository.product.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;

import javax.persistence.EntityManager;
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

    @BeforeAll
    void setUp(){

        Category category = Category.builder()
                .categoryId(1L)
                .category("Test")
                .build();

//      entityManager.persist(category);
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

//      entityManager.persist(productInfo);
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

//      entityManager.persist(product);
        productRepository.save(product);

        User user = User.builder()
                .email("jeyun")
                .password("1234")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();

//      entityManager.persist(user);
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
        String category = "Test";
        Boolean stock = true;
        Long minPrice = 0L;
        Long maxPrice = 1000000L;
        String keyword = "Test";
        String sort = "10대";

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
    @DisplayName("상품 상세 조회")
    void findProduct() {

        //WHEN
        Product product1 = productRepository.detail(1L);
        Product product2 = productRepository.findByProductId(1L);

        // THEN
        assertThat(product1).isEqualTo(product2);
    }

    @Test
    @DisplayName("상품 주문")
    void orderProduct() {

        // GIVEN
        Product product = productRepository.findByProductId(1L); // Stock = 10
        User user = userRepository.findByUserId(1L);
        Long orderNum = 3L;

        // WHEN
        product.getProductInfo().order(orderNum);
        Order order = new Order(product, orderNum, user);

        // THEN
        assertThat(order.getProduct().getProductInfo().getStock()).isEqualTo(7L);

    }
}