package product.service.cart;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import product.dto.product.ProductDetailResponseDto;
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.service.RedisTestContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceTest extends RedisTestContainer {

    @Autowired
    private CartRedisService cartRedisService;
    @Autowired
    private RedisTemplate<String, List<Long>> redisTemplate;

//    @BeforeAll
//    void setProduct(){
//        ArrayList<ProductDetailResponseDto> list = new ArrayList<>();
//
//        Category category = Category.builder() // 카테고리는 인덱스로 가져오기
//                .categoryId(1L)
//                .category("Test")
//                .build();
//
//        ProductInfo productInfo = ProductInfo.builder()
//                .productInfoId(1L)
//                .ten(10L)
//                .twenty(20L)
//                .thirty(30L)
//                .over_forty(40L)
//                .build();
//
//        Product product1 = Product.builder()
//                .title("1")
//                .content("Test_1")
//                .photo("Test_1")
//                .price(10000L)
//                .stock(10L)
//                .view(3)
//                .createdTime(LocalDateTime.now())
//                .category(category)
//                .productInfo(productInfo)
//                .build();
//    }

    @BeforeAll
    void flushall() {
        Objects.requireNonNull(redisTemplate.keys("*"))
                .forEach(k-> {redisTemplate.delete(k);
        });
    }

    @Test
    @DisplayName("카트에 상품 추가")
    void addCart() {

        // GIVEN
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        String key = "cart::Test";
        Long productId_1 = 11L;
        Long productId_2 = 33L;

        // WHEN
        cartRedisService.addCart(key, productId_1);
        cartRedisService.addCart(key, productId_2);
        List<Long> list = values.get("cart::Test");

        System.out.println(list);

        // THEN
        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    @DisplayName("카트에 상품 삭제")
    void deleteCart() {
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        List<Long> list = values.get("cart::Test");
        String key = "cart::Test";
        Long productId_1 = 11L;

        cartRedisService.deleteCart(key, productId_1);
        List<Long> list2 = values.get("cart::Test");

        Assertions.assertEquals(list2.get(0), 33L);
    }

    @Test
    @DisplayName("카트에 담긴 상품 리스트")
    void showCart() {

        String key = "cart::Test";
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        List<Long> list = cartRedisService.cartList(key);
        List<Long> list2 = values.get(key);

        Assertions.assertEquals(list, list2);
    }

    @Test
    @DisplayName("카트에 담긴  상품 주문")
    void orderCart() {
    }
}