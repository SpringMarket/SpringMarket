package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.Product;
import product.repository.product.ProductRepository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, ProductRankResponseDto> redisTemplateRankDto;
    private final ProductRepository productRepository;



    // 랭킹보드 조회
    public Set<ZSetOperations.TypedTuple<ProductRankResponseDto>> getRankingBoard(String key) {
        ZSetOperations<String, ProductRankResponseDto> ZSetOperations = redisTemplateRankDto.opsForZSet();
        return ZSetOperations.reverseRangeWithScores(key, 0, 99);
    }

    // 상품 조회수 증가
    public void incrementView(String key, Long productId) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        // key : [productView::1] -> value : [1]
        if(values.get(key) == null) {
            setView(key, String.valueOf(productRepository.getView(productId)), Duration.ofMinutes(35));
            values.increment(key);
        }
        else values.increment(key);
    }

    private void setView(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    // 상품 조회수 DB Update
    // ********* 조회수 업데이트 파이프라인 구축 예정 *********
    @Scheduled(cron = "0 0/100 * * * ?")
    @Transactional
    public void UpdateViewRDS() {
        Set<String> redisKeys = redisTemplate.keys("productView*");

        log.info("Starting View Update !");

        if (redisKeys == null) return;

        for (String data : redisKeys) {
            Long productId = Long.parseLong(data.split("::")[1]);
            int viewCnt = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(data)));

            productRepository.addView(productId, viewCnt);

            redisTemplate.delete(data);
        }
        log.info("Update View !");
    }
}


