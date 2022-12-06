package product.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import product.exception.RequestException;

import java.util.ArrayList;
import java.util.List;

import static product.exception.ExceptionType.*;

@RequiredArgsConstructor
@Repository
@Slf4j
public class CartRedisService {

    private final RedisTemplate<String, List<Long>> redisTemplate;

    public void setCart(String key, List<Long> list) {
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        values.set(key, list);
    }

    public void addCart(String key, Long productId){
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        if(values.get(key) == null) {
            List<Long> list = new ArrayList<>();
            list.add(productId);
            setCart(key, list);
        }
        else {
            List<Long> list = values.get(key);
            if (list.size()>=10) throw new RequestException(OVER_EXIST_EXCEPTION);
            if (list.contains(productId)) throw new RequestException(ALREADY_EXIST_EXCEPTION);
            list.add(productId);
            values.set(key, list);
        }
    }
    public void deleteCart(String key, Long productId){
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        if(values.get(key) == null) throw new RequestException(NOT_FOUND_KEY_EXCEPTION);
        else {
            List<Long> list = values.get(key);
            if (!list.contains(productId)) throw new RequestException(NOT_FOUND_EXCEPTION);
            list.remove(productId);
            values.set(key, list);
        }
    }

    public List<Long> cartList(String key){
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        List<Long> list = new ArrayList<>();

        if(values.get(key) == null) return list;
        else return values.get(key);
    }
}


