//
//package product.service.product;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//import product.dto.product.ProductMainResponseDto;
//import product.entity.product.Category;
//import product.entity.product.Product;
//import product.entity.product.ProductInfo;
//import product.repository.order.OrderRepository;
//import product.repository.product.CategoryRepository;
//import product.repository.product.ProductInfoRepository;
//import product.repository.product.ProductRepository;
//import product.repository.user.UserRepository;
//import product.service.TestConfig;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//// @BeforeAll 어노테이션 사용 시 @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//// EntityManager 의 .persist 사용 오류 -> < 해결 필요 >
//// + Repository 에 영속화 시키는 형식으로 변환
//
//@DataJpaTest
//@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@WebAppConfiguration
//@Import(TestConfig.class)
//@Rollback
//class ProductServiceTest {
//
//@Autowired
//ProductRepository productRepository;
//@Autowired
//CategoryRepository categoryRepository;
//@Autowired
//ProductInfoRepository productInfoRepository;
//
//@Autowired
//OrderRepository orderRepository;
//@Autowired
//UserRepository userRepository;
//
//// @Autowired
//// StockRepository stockRepository;
////
//// @Autowired
//// ViewRepository viewRepository;
//
//@BeforeAll
//void setUp(){
//
//Product product = Product.builder()
//.title("Test")
//.content("Test")
//.photo("Test")
//.price(10000L)
//.build();
//
//Category category = Category.builder() // 카테고리는 인덱스로 가져오기
//.categoryId(1L)
//.category("Test")
//.build();
//
//categoryRepository.save(category);
//
//ProductInfo productInfo = ProductInfo.builder()
//.productInfoId(1L)
//.ten(10L)
//.twenty(20L)
//.thirty(30L)
//.over_forty(40L)
//.build();
//
//productInfoRepository.save(productInfo);
//
//// Stock stock = Stock.builder()
//// .stock_id(1L)
//// .stock(10L)
//// .build();
////
//// stockRepository.save(stock);
////
//// View view = View.builder()
//// .view_id(1L)
//// .view(50)
//// .build();
////
//// viewRepository.save(view);
//
//product.setCategory(category);
//product.setProductInfo(productInfo);
//// product.setView(view);
//// product.setStock(stock);
//
//productRepository.save(product);
//}
//
//
//@Test
//@DisplayName("상품 캐싱")
//void warmup() {
//
////WHEN
//List<Product> warmupProduct = productRepository.warmup(1L);
//
//// THEN
//assertThat(warmupProduct.size()).isEqualTo(1);
//}
//
//
//@Test
//@DisplayName("상품 전체 조회")
//void findAllProduct() {
//
//// GIVEN
//Pageable pageable = Pageable.ofSize(10);
//String category = null;
//String stock = null;
//Long minPrice = null;
//Long maxPrice = null;
//String keyword = null;
//String sort = null;
//
//// WHEN
//Page<ProductMainResponseDto> list = productRepository.mainFilter(pageable, category, stock, minPrice, maxPrice, keyword, sort);
//
//// THEN
//assertThat(list.getTotalElements()).isEqualTo(1);
//}
//
//@Test
//@DisplayName("상품 전체 조회 - Null")
//void findAllProductNull() {
//
//// GIVEN
//Pageable pageable = Pageable.ofSize(10);
//String category = null;
//String stock = "1"; // Default 처리 필요
//Long minPrice = 10L;
//Long maxPrice = 1000000L;
//String keyword = "Test";
//String sort = "조회순";
//
//// WHEN
//Page<ProductMainResponseDto> list = productRepository.mainFilter(pageable, category, stock, minPrice, maxPrice, keyword, sort);
//
//// THEN
//assertThat(list.getTotalElements()).isEqualTo(1);
//}
//
//@Test
//@DisplayName("상품 상세 조회")
//void findProduct() {
//
////WHEN
//Product product1 = productRepository.detail(1L);
//Product product2 = productRepository.findByProductId(1L);
//
//// THEN
//assertThat(product1).isEqualTo(product2);
//}
//}
