package product.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.order.OrderRequestDto;
import product.dto.product.ProductResponseDetailDto;
import product.exception.RequestException;
import product.repository.product.ProductRepository;
import product.service.RedisService;
import product.service.order.OrderService;

import java.util.ArrayList;
import java.util.List;

import static product.exception.ExceptionType.ALREADY_EXIST_EXCEPTION;
import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final RedisService redisService;
    private final RedisTemplate<String, List<Long>> redisTemplate;


    // 장바구니에 추가
    @Transactional
    public void addCart(Long productId, Authentication authentication){

        if (productRepository.findByProductId(productId) == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        String key = "cart::" + authentication.getName(); // key : [cart:jeyun@naver.com] value : [1,2,3,4]
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();

        if(values.get(key) == null) {
            List<Long> list = new ArrayList<>();
            list.add(productId);
            redisService.setCart(key, list);
        }
        else {
            List<Long> list = values.get(key);
            if (list.contains(productId)) throw new RequestException(ALREADY_EXIST_EXCEPTION);
            list.add(productId);
            values.set(key, list);
        }
    }

    @Transactional
    public void deleteCart(Long productId, Authentication authentication){

        String key = "cart::" + authentication.getName();
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();

        if(values.get(key) == null) throw new RequestException(NOT_FOUND_EXCEPTION);
        else {
            List<Long> list = values.get(key);
            if (!list.contains(productId)) throw new RequestException(NOT_FOUND_EXCEPTION);
            list.remove(productId);
            values.set(key, list);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDetailDto> showCart(Authentication authentication) {
        String key = "cart::" + authentication.getName();

        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        List<ProductResponseDetailDto> dtos = new ArrayList<>();

        if(values.get(key) == null) return null;
        else {
            List<Long> list = values.get(key);
            for (Long cart : list) {
                ProductResponseDetailDto dto = ProductResponseDetailDto.toDto(productRepository.detail(cart));
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Transactional
    public void orderCart(Authentication authentication, List<OrderRequestDto> list) {

        if (list == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        String key = "cart::" + authentication.getName();
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        List<Long> productList = values.get(key);

        if (productList == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        for (OrderRequestDto order : list) {
            if (!productList.contains(order.getProductId())) throw new RequestException(NOT_FOUND_EXCEPTION);
            orderService.orderProduct(order.getProductId(), order.getOrderNum(), authentication);
        }
    }
}
