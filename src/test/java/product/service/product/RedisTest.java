package product.service.product;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import product.dto.product.ProductDetailResponseDto;
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.RedisTestContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RedisTest extends RedisTestContainer {

    @Autowired
    private RedisTemplate<String, ProductDetailResponseDto> redisTemplateDetail;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("Basic_Redis_Test")
    public void Basic_Redis_Test() {

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set("TE", "ST");

        assertEquals(values.get("TE"), "ST");
    }


    @BeforeAll
    public void Set_PipeLine() {

        ArrayList<ProductDetailResponseDto> list = new ArrayList<>();

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

        Product product1 = Product.builder()
                .title("1")
                .content("Test_1")
                .photo("Test_1")
                .price(10000L)
                .stock(10L)
                .view(3)
                .createdTime(LocalDateTime.now())
                .category(category)
                .productInfo(productInfo)
                .build();

        Product product2 = Product.builder()
                .title("2")
                .content("Test_2")
                .photo("Test_2")
                .price(10000L)
                .stock(10L)
                .view(4)
                .createdTime(LocalDateTime.now())
                .category(category)
                .productInfo(productInfo)
                .build();

        Product product3 = Product.builder()
                .title("3")
                .content("Test_3")
                .photo("Test_3")
                .price(10000L)
                .stock(10L)
                .view(5)
                .createdTime(LocalDateTime.now())
                .category(category)
                .productInfo(productInfo)
                .build();


        list.add(new ProductDetailResponseDto(product1));
        list.add(new ProductDetailResponseDto(product2));
        list.add(new ProductDetailResponseDto(product3));

        RedisSerializer keySerializer = redisTemplateDetail.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplateDetail.getValueSerializer();

        redisTemplateDetail.executePipelined((RedisCallback<Object>) connection -> {
            list.forEach(i -> {
                connection.set(keySerializer.serialize("Test::" + i.getTitle()),
                        valueSerializer.serialize(i));
            });
            return null;
        });
    }


    @Test
    @DisplayName("PipeLine_Test")
    public void PipeLine_Test() {
        ValueOperations<String, ProductDetailResponseDto> values = redisTemplateDetail.opsForValue();
        ProductDetailResponseDto dto1 = values.get("Test::1");
        ProductDetailResponseDto dto2 = values.get("Test::2");
        ProductDetailResponseDto dto3 = values.get("Test::3");

        System.out.println(dto1);

        assertEquals(values.get(("Test::1")), dto1);
        assertEquals(values.get(("Test::2")), dto2);
        assertEquals(values.get(("Test::3")), dto3);
    }

    @BeforeAll
    public void Set_ZSet_PipeLine() {

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        RedisSerializer keySerializer = redisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            list.forEach(i -> {
                connection.zSetCommands().zAdd(keySerializer.serialize("ZSet"),
                        Math.random() * 10, valueSerializer.serialize(i));
            });
            return null;
        });
    }

    @Test
    @DisplayName("PipeLine_ZSet_Test")
    public void PipeLine_ZSet_Test() {
        ZSetOperations<String, String> values = redisTemplate.opsForZSet();
        assertEquals(values.size("ZSet"), 4);
    }
}