package product.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.order.OrderRequestDto;
import product.dto.product.ProductMainResponseDto;
import product.exception.RequestException;
import product.repository.product.ProductRepository;
import product.service.order.OrderService;

import java.util.ArrayList;
import java.util.List;

import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService { // Redis 테스트 코드 : 제윤

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final CartRedisService cartRedisService;

    // 장바구니 추가 테스트
    @Transactional
    public void addCartTest(Long productId){
        String key = "cart::Test"; // key : [cart:jeyun@naver.com] value : [1,2,3,4]
        cartRedisService.addCart(key, productId);
    }

    // 장바구니에 추가
    @Transactional
    public void addCart(Long productId, Authentication authentication){

        if (productRepository.findByProductId(productId) == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        String key = "cart::" + authentication.getName(); // key : [cart:jeyun@naver.com] value : [1,2,3,4]
        cartRedisService.addCart(key, productId);
    }

    @Transactional
    public void deleteCart(Long productId, Authentication authentication){

        String key = "cart::" + authentication.getName();
        cartRedisService.deleteCart(key, productId);
    }

    @Transactional(readOnly = true)
    public List<ProductMainResponseDto> showCart(Authentication authentication) {
        String key = "cart::" + authentication.getName();

        List<ProductMainResponseDto> productMainResponseDtos = new ArrayList<>();
        List<Long> list = cartRedisService.cartList(key);

        for (Long cart : list) {
            ProductMainResponseDto dto = ProductMainResponseDto.toDto(productRepository.detail(cart));
            productMainResponseDtos.add(dto);
        }
        return productMainResponseDtos;
    }

    @Transactional
    public void orderCart(Authentication authentication, List<OrderRequestDto> list) {

        if (list == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        String key = "cart::" + authentication.getName();

        List<Long> cartList = cartRedisService.cartList(key);

        for (OrderRequestDto order : list) {
            if (!cartList.contains(order.getProductId())) throw new RequestException(NOT_FOUND_EXCEPTION);
            orderService.orderProduct(order.getProductId(), order.getOrderNum(), authentication);
        }
    }
}
