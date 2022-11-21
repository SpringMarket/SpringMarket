package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductCreateDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductDetailResponseDto;
import product.entity.product.*;
import product.exception.RequestException;
import product.repository.product.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRedisService productRedisService;
    private final RedisTemplate<String, String> redisTemplate;
    private final CategoryRepository categoryRepository;
    private final ProductInfoRepository productInfoRepository;



    // Warm UP -> Named Post Put !
    @Transactional
    public void warmup() {

        log.info("Warm Up Start....");

        List<Product> warmupProduct = new ArrayList<>();

        for (long k =1; k<6; k++) {
            List<Product> list = productRepository.warmup(k);
            warmupProduct.addAll(list);
        }

        for (Product product : warmupProduct) {
            productRedisService.setProduct("product::" + product.getProductId(), ProductDetailResponseDto.toDto(product), Duration.ofDays(1));
        }

        log.info("..... Success!");
    }



    // 상품 전체 조회
    @Transactional(readOnly = true)
    public Page<ProductMainResponseDto> findAllProduct(Pageable pageable, String category, String stock, Long minPrice, Long maxPrice, String keyword, String sort) {

        log.info("Search All Log Start....");

        return productRepository.mainFilter(pageable, category,  stock, minPrice, maxPrice, keyword, sort);
    }


    // 상품 상세 조회 -> Cache Aside
    @Cacheable(value = "product", key = "#id") // [product::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public ProductDetailResponseDto findProduct(Long id) {

        log.info("Search Once Log Start....");
        Product product = productRepository.detail(id);
        if (product == null ) throw new RequestException(NOT_FOUND_EXCEPTION);

        return ProductDetailResponseDto.toDto(product);
    }


    // 상품 조회수 추가
    public void countView(Long productId) {
        String key = "productView::" + productId;

        log.info("View Start");  // productView::1 -> 1

        ValueOperations<String, String> values = redisTemplate.opsForValue(); // Redis String 자료구조 저장소 선언

        if(values.get(key) == null) {
            productRedisService.setView(key, String.valueOf(productRepository.getView(productId)), Duration.ofMinutes(35));
            values.increment(key);
        }
        else values.increment(key);

        log.info("View" + values.get(key));
    }


    // 상품 데이터 생성 :: 더미
    public void create(ProductCreateDto pc){


        Category category = Category.builder() // 카테고리는 인덱스로 가져오기
                .categoryId(1L)
                .category("상의")
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

        Product product = Product.builder()
                .title(pc.getTitle())
                .content(pc.getContent())
                .photo(pc.getPhoto())
                .price(pc.getPrice())
                .category(category)
                .productInfo(productInfo)
                .view(0)
                .stock(100L)
                .build();

        productRepository.save(product);

    }
}
