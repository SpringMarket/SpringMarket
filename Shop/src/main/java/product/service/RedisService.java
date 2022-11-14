package product.service;

import org.springframework.scheduling.annotation.Scheduled;
import product.dto.product.ProductResponseDetailDto;
import product.entity.product.Product;
import product.exception.ExceptionType;
import product.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import product.repository.product.ProductRepository;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, ProductResponseDetailDto> redisTemplateDto;
    private final ProductRepository productRepository;

    public void setView(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        Duration duration =Duration.ofMinutes(15);
        values.set(key, data, duration);
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void UpdateViewRDS() {
        Set<String> redisKeys = redisTemplate.keys("productView*");

        assert redisKeys != null;

        for (String data : redisKeys) {
            Long productId = Long.parseLong(data.split("::")[1]);
            Long viewCnt = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(data)));

            productRepository.addView(productId, viewCnt);
        }
    }

    public void setProduct(String key, ProductResponseDetailDto data, Duration duration) {
        ValueOperations<String, ProductResponseDetailDto> values = redisTemplateDto.opsForValue();
        values.set(key, data, duration);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
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


