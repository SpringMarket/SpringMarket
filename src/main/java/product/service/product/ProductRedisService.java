package product.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductResponseDto;
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
    private final RedisTemplate<String, ProductResponseDto> redisTemplateDto;
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

    public void setProduct(String key, ProductResponseDto data, Duration duration) {
        ValueOperations<String, ProductResponseDto> values = redisTemplateDto.opsForValue();
        values.set(key, data, duration);
    }

}


