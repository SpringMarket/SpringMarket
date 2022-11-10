package product.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.product.CategoryRepository;
import product.repository.product.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.service.RedisService;

import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;

@SpringBootTest
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
    RedisService redisService;

    @BeforeEach
    void create(){

        Category category = new Category
                ("Example");

        categoryRepository.save(category);

        ProductInfo productInfo = new ProductInfo
                (10L, 9L, 8L, 7L, 999L, 999L);

        productInfoRepository.save(productInfo);

        Product product = new Product(
                "Hi",
                "There",
                "Photo",
                99999L,
                LocalDateTime.now().minusYears(1),
                category,
                productInfo
        );

        productRepository.save(product);
    }


//    @Test
//    void warmup() {
//
//    }
//
//    @Test
//    void findAllProduct() {
//
//
//
//    }

    @Test
    @Transactional
    @DisplayName("상품 상세 조회")
    void findProduct() {

        Product product1 = productRepository.detail(1L);
        Product product2 = productRepository.findById(1L)
                .orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));

        Assertions.assertThat(product1).isEqualTo(product2);
    }

//    @Test
//    void orderProduct() {
//    }
}