package product.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CartRedisService {

    private final RedisTemplate<String, List<Long>> redisTemplate2;

    public void setCart(String key, List<Long> list) {
        ValueOperations<String, List<Long>> values = redisTemplate2.opsForValue();

        values.set(key, list);
    }

}


