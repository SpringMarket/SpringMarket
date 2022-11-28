package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.Product;
import product.exception.RequestException;
import product.repository.product.ProductRepository;

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


    // 랭킹보드 조회
    public List<ProductRankResponseDto> getRankingList(Long categoryId) {
        String key = "Ranking::" + categoryId;
        Set<ZSetOperations.TypedTuple<ProductRankResponseDto>> typedTuples = productRedisService.getRankingBoard(key);

        if (typedTuples == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        return typedTuples.stream().map(ProductRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }


    // 상품 필터링 조회
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
        ProductDetailResponseDto product = productRepository.detail(id);
        if (product == null ) throw new RequestException(NOT_FOUND_EXCEPTION);
        countView(id);
        return product;
    }


    // 상품 조회수 추가
    public void countView(Long productId) {
        String key = "productView::" + productId;
        log.info("View Increment");
        productRedisService.incrementView(key, productId);
    }
}
