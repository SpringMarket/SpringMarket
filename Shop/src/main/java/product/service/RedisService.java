package product.service;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductResponseDto;
import product.exception.ExceptionType;
import product.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import product.repository.product.ProductRepository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, List<Long>> redisTemplate2;
    private final RedisTemplate<String, ProductResponseDto> redisTemplateDto;
    private final ProductRepository productRepository;

    public void setView(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void UpdateViewRDS() {
        Set<String> redisKeys = redisTemplate.keys("productView*");

        log.info("Starting View Update !");

        assert redisKeys != null;

        for (String data : redisKeys) {
            Long productId = Long.parseLong(data.split("::")[1]);
            Long viewCnt = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(data)));

            productRepository.addView(productId, viewCnt);

            redisTemplate.delete(data);
        }
        log.info("Update View !");
    }

    public void setProduct(String key, ProductResponseDto data, Duration duration) {
        ValueOperations<String, ProductResponseDto> values = redisTemplateDto.opsForValue();
        values.set(key, data, duration);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public void setCart(String key, List<Long> list) {
        ValueOperations<String, List<Long>> values = redisTemplate2.opsForValue();

        values.set(key, list);
    }

    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void checkRefreshToken(String username, String refreshToken) {
        String redisRT = this.getValues(username);
        if(!refreshToken.equals(redisRT)) {
            throw new RequestException(ExceptionType.TOKEN_EXPIRED_EXCEPTION);
        }
    }

}


