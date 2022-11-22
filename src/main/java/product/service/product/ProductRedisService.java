package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.repository.product.ProductRepository;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, ProductDetailResponseDto> redisTemplateDetailDto;
    private final RedisTemplate<String, ProductMainResponseDto> redisTemplateMainDto;
    private final ProductRepository productRepository;

    public void setView(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional
    public void UpdateViewRDS() {
        Set<String> redisKeys = redisTemplate.keys("productView*");

        log.info("Starting View Update !");

        assert redisKeys != null;

        for (String data : redisKeys) {
            Long productId = Long.parseLong(data.split("::")[1]);
            int viewCnt = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(data)));

            productRepository.addView(productId, viewCnt);

            redisTemplate.delete(data);
        }
        log.info("Update View !");
    }

    public void setProduct(String key, ProductDetailResponseDto data, Duration duration) {
        ValueOperations<String, ProductDetailResponseDto> values = redisTemplateDetailDto.opsForValue();
        values.set(key, data, duration);
    }

    public void setRankingBoard(String key, ProductMainResponseDto data, double score) {
        ZSetOperations<String, ProductMainResponseDto> values = redisTemplateMainDto.opsForZSet();
        values.add(key, data, score);
    }

    public ZSetOperations<String, ProductMainResponseDto> getRankingBoard(String key, double start, double end) {
        ZSetOperations<String, ProductMainResponseDto> values = redisTemplateMainDto.opsForZSet();
        values.reverseRange(key, 0, 99);
        return values;
    }
}


