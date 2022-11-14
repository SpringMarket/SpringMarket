package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductResponseDetailDto;
import product.entity.order.Order;
import product.entity.product.Product;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.order.OrderRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;
import product.service.RedisService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RedisService redisService;

    private final RedisTemplate<String, String> redisTemplate;


    /*@Value("${cloud.aws.s3.bucket}")
    String bucket;*/


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
            redisService.setProduct("product::" + product.getProductId(), ProductResponseDetailDto.toDto(product), Duration.ofDays(1));
        }

        log.info("..... Success!");
    }



    // 상품 전체 조회
    @Transactional(readOnly = true)
    public Page<ProductResponseDetailDto> findAllProduct(Pageable pageable, String category, Boolean stock, Long minPrice, Long maxPrice, String keyword, String sort) {

        log.info("Search All Log Start....");

        return productRepository.mainFilter(pageable, category,  stock, minPrice, maxPrice, keyword, sort);
    }


    // 상품 상세 조회 -> Cache Aside
    @Cacheable(value = "product", key = "#id") // [post::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public ProductResponseDetailDto findProduct(Long id) {

        log.info("Search Once Log Start....");
        Product product = productRepository.detail(id);

        return ProductResponseDetailDto.toDto(product);
    }

    public void countView(Long productId) {
        String key = "productView::" + productId;

        ValueOperations<String, String> values = redisTemplate.opsForValue();

        // query 로직으로 수정해야함
        if(values.get(key) == null) redisService.setView(key, String.valueOf(productRepository.getView(productId)));
        else values.increment(key);

        log.info("View" + values.get(key));
    }

}
