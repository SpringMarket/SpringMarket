package product.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import product.MysqlTestContainer;
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.repository.product.CategoryRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest extends MysqlTestContainer {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductInfoRepository productInfoRepository;



    @Test
    @DisplayName("랭킹보드 조회 -> Default")
    void getRankingList() {
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

        Product product1 = Product.builder()
                .title("1")
                .content("Test_1")
                .photo("Test_1")
                .price(10000L)
                .stock(10L)
                .view(3)
                .createdTime(LocalDateTime.now())
                .categoryId(1L)
                .productInfo(productInfo)
                .build();

        productRepository.save(product1);

        Product product2 = productRepository.findByTitle("1");

        System.out.println("ID 1 : " + product1.getProductId());
        System.out.println("ID 2 : " + product2.getProductId());
        System.out.println("ID 1 : " + product2.getTitle());
        System.out.println("ID 2 : " + product2.getTitle());

        assertEquals(product1.getTitle(), product2.getTitle());


    }

    @Test
    @DisplayName("랭킹보드 조회 -> Not Exist Data")
    void getRankingListNotExistData() {
    }



    @Test
    @DisplayName("상품 필터링 조회 -> Default")
    void findAllProduct() {
    }

    @Test
    @DisplayName("상품 필터링 조회 -> Filter Null")
    void findAllProductFilterNull() {
    }



    @Test
    @DisplayName("상품 상세 조회 -> Default")
    void findProduct() {
    }

    @Test
    @DisplayName("상품 상세 조회 -> Cache Hit")
    void findProductCacheHit() {
    }

    @Test
    @DisplayName("상품 상세 조회 -> Not Exist Data")
    void findProductNotExistProduct() {
    }



    @Test
    @DisplayName("상품 조회수 추가 -> Exist Redis Key")
    void countViewExistKey() {}

    @Test
    @DisplayName("상품 조회수 추가 -> Not Exist Redis Key")
    void countViewNotExistKey() {}
}