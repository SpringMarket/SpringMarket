package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductDetailResponseDto;
import product.entity.product.*;
import product.exception.RequestException;
import product.repository.product.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRedisService productRedisService;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, ProductMainResponseDto> redisTemplateMainDto;



    // Warm UP -> Named Post
    // 테스트코드 : 제윤
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

    // Warm UP -> Ranking Board
    @Transactional
    public void warmupRank() {
        for (long i=1; i<6; i++){
            List<Product> list = productRepository.warmup(i);
            for (int k = 0; k < 99; k++) {
                productRedisService.setRankingBoard("ranking::"+i, ProductMainResponseDto.toDto(list.get(k)), list.get(k).getView());
            }
        }
    }

    // 랭킹보드 조회
    public List<ProductMainResponseDto> getRankingList(Long categoryId) {
        String key = "ranking::" + categoryId;
        ZSetOperations<String, ProductMainResponseDto> stringStringZSetOperations = redisTemplateMainDto.opsForZSet();
        Set<ZSetOperations.TypedTuple<ProductMainResponseDto>> typedTuples = stringStringZSetOperations.reverseRangeWithScores(key, 0, 99);

        if (typedTuples == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        return typedTuples.stream().map(ProductMainResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }


    // 상품 필터링 조회
    @Transactional(readOnly = true)
    public Page<ProductMainResponseDto> findAllProduct(Pageable pageable, String category, String stock, Long minPrice, Long maxPrice, String keyword, String sort) {

        log.info("Search All Log Start....");

        return productRepository.mainFilter(pageable, category,  stock, minPrice, maxPrice, keyword, sort);
    }


    // 상품 상세 조회 -> Cache Aside
    // 테스트코드 : 제윤
    @Cacheable(value = "product", key = "#id") // [product::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public ProductDetailResponseDto findProduct(Long id) {

        log.info("Search Once Log Start....");
        Product product = productRepository.detail(id);
        if (product == null ) throw new RequestException(NOT_FOUND_EXCEPTION);
        countView(id);
        return ProductDetailResponseDto.toDto(product);
    }


    // 상품 조회수 추가
    // 테스트코드 : 제윤
    public void countView(Long productId) {
        String key = "productView::" + productId;

        log.info("View Start");  // key : [productView::1] -> value : [1]

        ValueOperations<String, String> values = redisTemplate.opsForValue(); // Redis String 자료구조 저장소 선언

        if(values.get(key) == null) {
            productRedisService.setView(key, String.valueOf(productRepository.getView(productId)), Duration.ofMinutes(35));
            values.increment(key);
        }
        else values.increment(key);

        log.info("View" + values.get(key));
    }
}
