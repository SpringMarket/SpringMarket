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
import product.repository.product.ProductRepository;
import product.service.RedisService;
import product.service.order.OrderService;

import java.util.ArrayList;
import java.util.List;

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
    public String addCart(Long productId, Authentication authentication){

        if (productRepository.findByProductId(productId) == null) return "선택하신 상품이 존재하지 않습니다.";

        String key = "cart::" + authentication.getName(); // key : [cart:jeyun@naver.com] value : [1,2,3,4]

        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();

        if(values.get(key) == null) {
            List<Long> list = new ArrayList<>();
            list.add(productId);
            redisService.setCart(key, list);
        }
        else {
            List<Long> list = values.get(key);

            if (list.contains(productId)) return "이미 장바구니에 포함된 상품입니다.";

            list.add(productId);
            values.set(key, list);
        }
        return "장바구니에 추가되었습니다.";
    }

    @Transactional
    public String deleteCart(Long productId, Authentication authentication){

        String key = "cart::" + authentication.getName();

        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();

        if(values.get(key) == null) return "장바구니에 상품이 존재하지 않습니다.";
        else {
            List<Long> list = values.get(key);

            if (!list.contains(productId)) return "장바구니에 상품이 존재하지 않습니다.";

            list.remove(productId);
            values.set(key, list);
        }
        return "장바구니에서 상품이 삭제되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDetailDto> showCart(Authentication authentication) {
        String key = "cart::" + authentication.getName();

        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();

        List<ProductResponseDetailDto> dtos = new ArrayList<>();

        if(values.get(key) == null) return null;
        else {
            List<Long> list = values.get(key);
            for (int k=0; k<list.size(); k++){
                ProductResponseDetailDto dto = ProductResponseDetailDto.toDto(productRepository.detail(list.get(k)));
                dtos.add(dto);
            }
        }

        return dtos;
    }

    @Transactional
    public String orderCart(Authentication authentication, List<OrderRequestDto> list) {

        if (list == null) return "선택된 상품이 없습니다.";

        String key = "cart::" + authentication.getName();
        ValueOperations<String, List<Long>> values = redisTemplate.opsForValue();
        List<Long> productList = values.get(key);

        if (productList == null) return "장바구니에 상품이 없습니다.";

        for (OrderRequestDto order : list) {
            if (!productList.contains(order.getProductId())) return "주문하려는 상품이 장바구니에 없습니다.";
            orderService.orderProduct(order.getProductId(), order.getOrderNum(), authentication);
        }
        return "주문이 성공적으로 진행되었습니다.";
    }

}
