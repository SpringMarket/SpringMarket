package product.service;

import product.dto.product.ProductResponseDetailDto;
import product.exception.ExceptionType;
import product.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, ProductResponseDetailDto> redisTemplateDto;

    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
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


