package product.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import product.exception.ExceptionType;
import product.exception.RequestException;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }


    public void checkRefreshToken(String username, String refreshToken) {
        String redisRT = this.getValues(username);
        if(!refreshToken.equals(redisRT)) {
            throw new RequestException(ExceptionType.TOKEN_EXPIRED_EXCEPTION);
        }
    }

}



