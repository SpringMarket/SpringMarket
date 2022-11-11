package product.service.mypage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.user.Authority;
import product.entity.user.User;
import product.repository.product.CategoryRepository;
import product.repository.product.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;
import product.service.product.TestConfig;

import static org.junit.jupiter.api.Assertions.*;

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
    void myPage() {
    }

    @Test
    void cancel() {
    }
}