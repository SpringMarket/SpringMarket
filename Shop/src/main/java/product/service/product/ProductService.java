package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductResponseDetailDto;
import product.entity.product.Order;
import product.entity.product.Product;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.product.OrderRepository;
import product.repository.product.ProductRepository;
import product.service.RedisService;

import java.time.Duration;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final RedisService redisService;


    @Value("${cloud.aws.s3.bucket}")
    String bucket;



    // Warm UP -> Named Post Put !
    @Transactional(readOnly = true)
    public void warmupCandy() {

        log.info("Warm Up Start....");

        List<Product> candyCandy =productRepository.warmup();

        for (Product product : candyCandy) {
            redisService.setProduct("product::" + product.getProductId(), ProductResponseDetailDto.toDto(product), Duration.ofDays(1));
        }

        log.info("..... Success!");
    }



    // 사탕 전체 조회
    @Transactional(readOnly = true)
    public Page<ProductResponseDetailDto> findAllProduct(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword, String sort) {

        log.info("Search All Log Start....");

        return productRepository.mainFilter(pageable, category,  stock, price, age, keyword, sort);
    }


    // 사탕 상세 조회 -> Cache Aside
    @Cacheable(value = "product", key = "#id") // [post::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public ProductResponseDetailDto findProduct(Long categoryId, Long id) {

        log.info("Search Once Log Start....");
        Product product = productRepository.detail(categoryId, id);

        return ProductResponseDetailDto.toDto(product);
    }

    // 사탕 주문
    @Transactional(readOnly = true) // -> start
    public void orderProduct(Long id, Long orderNum, User user) {

        log.info("Order Start....");

        Product product = productRepository.findByProductId(id).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));
        product.order(orderNum);

        Order order = new Order(product, orderNum, user);

        orderRepository.save(order);
    }
}

