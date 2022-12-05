package product.service.admin;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import product.RedisTestContainer;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.repository.order.OrderRepository;
import product.repository.product.CategoryRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;
import product.service.product.ProductService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminServiceTest extends RedisTestContainer {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate<String, ProductDetailResponseDto> redisTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @BeforeAll
    @DisplayName("<Before> 상품 초기화")
    void setProduct(){

        ProductInfo productInfo_1 = ProductInfo.builder()
                .productInfoId(1L)
                .ten(10L)
                .twenty(20L)
                .thirty(30L)
                .over_forty(40L)
                .build();

        ProductInfo productInfo_2 = ProductInfo.builder()
                .productInfoId(2L)
                .ten(11L)
                .twenty(21L)
                .thirty(31L)
                .over_forty(41L)
                .build();
        ProductInfo productInfo_3 = ProductInfo.builder()
                .productInfoId(3L)
                .ten(12L)
                .twenty(22L)
                .thirty(32L)
                .over_forty(42L)
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
                .productInfo(productInfo_1)
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
                .productInfo(productInfo_2)
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
                .productInfo(productInfo_3)
                .build();

        productInfoRepository.save(productInfo_1);
        productInfoRepository.save(productInfo_2);
        productInfoRepository.save(productInfo_3);
        productRepository.save(product_1);
        productRepository.save(product_2);
        productRepository.save(product_3);

        // Clean Cache
        Objects.requireNonNull(redisTemplate.keys("*"))
                .forEach(k-> {redisTemplate.delete(k);
                });
    }

    @Test
    void warmupPipeLine() {
        adminService.warmupPipeLine(1L);
        ValueOperations<String, ProductDetailResponseDto> values = redisTemplate.opsForValue();
        ProductDetailResponseDto dto_1 = values.get("product::1");
        ProductDetailResponseDto dto_2 = values.get("product::2");
        ProductDetailResponseDto dto_3 = values.get("product::3");

        assertEquals(dto_1.getProductId(), 1L);
        assertEquals(dto_2.getProductId(), 2L);
        assertEquals(dto_3.getProductId(), 3L);
    }

    @Test
    void warmupRankingPipeLine() {
        adminService.warmupRankingPipeLine(1L);

        List<ProductRankResponseDto> list1 = productService.getRankingList(1L, 1L);

        assertEquals(list1.get(0).getProductId(), 3);
        assertEquals(list1.get(1).getProductId(), 2);
        assertEquals(list1.get(2).getProductId(), 1);

    }
}