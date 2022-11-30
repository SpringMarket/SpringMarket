package product.service.cart;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import product.RedisTestContainer;

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
    void flushallBefore() {
        Objects.requireNonNull(redisTemplate.keys("*"))
                .forEach(k-> {redisTemplate.delete(k);
        });
    }

    @AfterAll
    void flushallAfter() {
        Objects.requireNonNull(redisTemplate.keys("*"))
                .forEach(k-> {redisTemplate.delete(k);
                });
    }



    @Test
    @DisplayName("카트에 상품 추가 -> Default")
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
    @DisplayName("카트에 상품 추가 -> Already Exist Data")
    void addCartAlreadyExistData(){}

    @Test
    @DisplayName("카트에 상품 추가 -> Not Exist Data")
    void addCartNotExistData(){}



    @Test
    @DisplayName("카트 상품 삭제 -> Default")
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
    @DisplayName("카트 상품 삭제 -> Not Exist Redis Key")
    void deleteCartNotExistKey(){}

    @Test
    @DisplayName("카트 상품 삭제 -> Not Exist Data")
    void deleteCartNotExistData(){}



    @Test
    @DisplayName("카트 조회 -> Default")
    void showCart() {

        String key = "cart::Test";
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        List<Long> list = cartRedisService.cartList(key);
        List<Long> list2 = values.get(key);

        Assertions.assertEquals(list, list2);
    }

    @Test
    @DisplayName("카트 조회 -> Not Exist Redis Key")
    void showCartNotExistKey(){}



    @Test
    @DisplayName("카트에 담긴 상품 주문 -> Default")
    void orderCart() {}

    @Test
    @DisplayName("카트에 담긴 상품 주문 -> 주문 요청이 존재하지 않음")
    void orderCartNotExistRequest() {}

    @Test
    @DisplayName("카트에 담긴 상품 주문 -> 카트에 든 상품과 요청이 일치하지 않음")
    void orderCartNotMatchCart() {}
}