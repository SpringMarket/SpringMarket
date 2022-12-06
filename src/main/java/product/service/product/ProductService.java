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
import product.entity.product.Category;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
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
    public List<ProductRankResponseDto> getRankingList(Long categoryId, Long preferId) {

        if (categoryId > 5) throw new RequestException(NOT_FOUND_EXCEPTION);

        String key = "Ranking::" + categoryId + "::" + preferId;
        Set<ZSetOperations.TypedTuple<ProductRankResponseDto>> typedTuples = productRedisService.getRankingBoard(key);

        return typedTuples.stream().map(ProductRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }


    // 상품 필터링 조회
    @Transactional(readOnly = true)
    public Page<ProductMainResponseDto> findAllProduct(Pageable pageable, Long categoryId, String stock, Long minPrice, Long maxPrice, String keyword, String sort) {

        log.info("Search All Log Start....");

        return productRepository.mainFilter(pageable, categoryId,  stock, minPrice, maxPrice, keyword, sort);
    }

    // 상품 키워드 조회
    @Transactional(readOnly = true)
    public Page<ProductMainResponseDto> findByKeyword(Pageable pageable, String keyword) {
        return productRepository.keywordFilter(pageable, keyword);
    }


    // 상품 상세 조회 -> Cache Aside
    @Cacheable(value = "product", key = "#id") // [product::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public ProductDetailResponseDto findProduct(Long id) {

        log.info("Search Once Log Start....");
        ProductDetailResponseDto product = productRepository.detail(id);
        if (product == null) throw new RequestException(NOT_FOUND_EXCEPTION);
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
