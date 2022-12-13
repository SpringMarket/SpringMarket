package product.service.product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import product.MysqlTestContainer;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.exception.RequestException;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.service.admin.AdminService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest extends MysqlTestContainer {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    AdminService adminService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductRedisService productRedisService;
    @Autowired
    private RedisTemplate<String, ProductDetailResponseDto> redisTemplate;
    @Autowired
    private RedisTemplate<String, String> StringredisTemplate;


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
                .view(10)
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
                .view(10)
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
                .view(10)
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


        // Caching
        adminService.warmupPipeLine(1L);
        adminService.warmupRankingPipeLine(1L);
    }

    @Test
    @DisplayName("랭킹보드 조회 -> Default")
    void getRankingList() {
        List<ProductRankResponseDto> list =  productService.getRankingList(1L, 1L);

        assertEquals(list.get(0).getProductId(), 3L);
        assertEquals(list.get(1).getProductId(), 2L);
        assertEquals(list.get(2).getProductId(), 1L);
    }

    @Test
    @DisplayName("랭킹보드 조회 -> Not Exist Data")
    void getRankingListNotExistData() {

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            productService.getRankingList(999L, 1L); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);
    }



    @Test
    @DisplayName("상품 필터링 조회 -> Default")
    void findAllProduct() {

        Pageable pageable = Pageable.ofSize(10);
        Long categoryId = 1L;
        String stock = "1";
        Long minPrice = 0L;
        Long maxPrice = 99999L;
        String keyword = "Test";
        String sort = "날짜순";
        Page<ProductMainResponseDto> list =  productService.findAllProduct(pageable, categoryId, stock, minPrice, maxPrice, keyword, sort);

        assertEquals(list.getTotalElements(),3);
    }

    @Test
    @DisplayName("상품 필터링 조회 -> Filter Null")
    void findAllProductFilterNull() {
        Pageable pageable = Pageable.ofSize(10);
        Long categoryId = null;
        String stock = null;
        Long minPrice = null;
        Long maxPrice = null;
        String keyword = null;
        String sort = null;
        Page<ProductMainResponseDto> list =  productService.findAllProduct(pageable, categoryId, stock, minPrice, maxPrice, keyword, sort);

        assertEquals(list.getTotalElements(),3);

    }
    @Test
    @DisplayName("상품 필터링 조회 -> Empty Result")
    void findAllProductEmpty() {

        Pageable pageable = Pageable.ofSize(10);
        Long categoryId = 3L;
        String stock = "1";
        Long minPrice = 0L;
        Long maxPrice = 1000L;
        String keyword = "Test";
        String sort = "조회순";

        RequestException exception = assertThrows(RequestException.class, ()-> {
            productService.findAllProduct(pageable, categoryId, stock, minPrice, maxPrice, keyword, sort); });
        String message = exception.getMessage();

        // THEN
        assertEquals("조회된 상품이 없습니다.", message);

    }

    @Test
    @DisplayName("상품 키워드 조회 -> Keyword zero word")
    void findByKeywordZeroWord() {
        Pageable pageable = Pageable.ofSize(10);
        String keyword = "";

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            productService.findByKeyword(pageable, keyword); });
        String message = exception.getMessage();

        // THEN
        assertEquals("키워드를 작성해주세요.", message);
    }

    @Test
    @DisplayName("상품 키워드 조회 -> Keyword one word")
    void findByKeywordOneWord() {
        Pageable pageable = Pageable.ofSize(10);
        String keyword = "하";

        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            productService.findByKeyword(pageable, keyword); });
        String message = exception.getMessage();

        // THEN
        assertEquals("두 글자 이상부터 검색이 가능합니다.", message);
    }

    @Test
    @DisplayName("상품 상세 조회 -> Default")
    void findProduct() {

        ProductInfo productInfo = ProductInfo.builder()
                .productInfoId(1L)
                .ten(10L)
                .twenty(20L)
                .thirty(30L)
                .over_forty(40L)
                .build();

        Product product = Product.builder()
                .productId(4L)
                .title("Test_3")
                .content("Test_3")
                .photo("Test_3")
                .price(10000L)
                .stock(10L)
                .view(3)
                .createdTime(LocalDateTime.now())
                .categoryId(1L)
                .productInfo(productInfo)
                .build();


        productInfoRepository.save(productInfo);
        productRepository.save(product);

        ProductDetailResponseDto productDetailResponseDto = productService.findProduct(4L);

        assertEquals(productDetailResponseDto.getProductId(), 4L);
    }

    @Test
    @DisplayName("상품 상세 조회 -> Cache Hit")
    void findProductCacheHit() {
        ValueOperations<String, ProductDetailResponseDto> values = redisTemplate.opsForValue();
        ProductDetailResponseDto productCache = values.get("product::1");
        System.out.println(productCache);

        ProductDetailResponseDto productDetailResponseDto = productService.findProduct(1L);
        System.out.println(productDetailResponseDto);

        assertEquals(productCache.getProductId(), productDetailResponseDto.getProductId());
    }

    @Test
    @DisplayName("상품 상세 조회 -> Not Exist Data")
    void findProductNotExistProduct() {
        // WHEN
        RequestException exception = assertThrows(RequestException.class, ()-> {
            productService.findProduct(999L); });
        String message = exception.getMessage();

        // THEN
        assertEquals("요청하신 자료를 찾을 수 없습니다.", message);
    }



    @Test
    @DisplayName("상품 조회수 추가 -> Exist Redis Key")
    void countViewExistKey() {
        ValueOperations<String, String> values = StringredisTemplate.opsForValue();
        StringredisTemplate.delete("productView::1");
        productService.countView(1L);
        productService.countView(1L);

        assertEquals(values.get("productView::1"), "12");
    }

    @Test
    @DisplayName("상품 조회수 추가 -> Not Exist Redis Key")
    void countViewNotExistKey() {
        ValueOperations<String, String> values = StringredisTemplate.opsForValue();
        StringredisTemplate.delete("productView::1");
        productService.countView(1L);

        assertEquals(values.get("productView::1"), "11");
    }

    @Test
    @DisplayName("조회수 DB 업데이트")
    void updateDB(){
        ValueOperations<String, String> values = StringredisTemplate.opsForValue();
        Product product_1 = productRepository.findByProductId(1L);
        int view_1 = product_1.getView();

        StringredisTemplate.delete("productView::1");
        productService.countView(1L);
        productService.countView(1L);

        String view = values.get("productView::1");

//        productRedisService.UpdateViewRDS();
//
//        Product product_2 = productRepository.findByProductId(1L);
//        int view_2 = product_2.getView();

        assertEquals(view_1, 10);
        assertEquals(view, "12");
    }

    @Test
    @DisplayName("조회수 DB 업데이트 -> key x")
    void updateDBNotExistKey(){

        Objects.requireNonNull(redisTemplate.keys("productView*"))
                .forEach(k-> {redisTemplate.delete(k);
                });

        Product product_1 = productRepository.findByProductId(1L);
        int view_1 = product_1.getView();

        productRedisService.UpdateViewRDS();

        Product product_2 = productRepository.findByProductId(1L);
        int view_2 = product_2.getView();

        assertEquals(view_1, 10);
        assertEquals(view_2, 10);
    }
}