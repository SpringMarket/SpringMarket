package product.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import static product.exception.ExceptionType.OVER_EXIST_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService { // Redis 테스트 코드 : 제윤

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final CartRedisService cartRedisService;


    // 장바구니에 추가
    @Transactional(readOnly = true)
    public void addCart(Long productId, Authentication authentication){
        // 유효성 검사를 어디서?
        if (productRepository.findByProductId(productId) == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        String key = "cart::" + authentication.getName(); // key : [cart:jeyun@naver.com] value : [1,2,3,4]
        cartRedisService.addCart(key, productId);
    }


    public void deleteCart(Long productId, Authentication authentication){

        String key = "cart::" + authentication.getName();
        cartRedisService.deleteCart(key, productId);
    }

    @Transactional(readOnly = true)
    public List<ProductMainResponseDto> showCart(Authentication authentication, Pageable pageable) {
        String key = "cart::" + authentication.getName();

        List<Long> list = cartRedisService.cartList(key);

        return productRepository.cartList(list, pageable);
    }

    @Transactional
    public void orderCart(Authentication authentication, List<OrderRequestDto> list) {

        if (list.size() == 0) throw new RequestException(NOT_FOUND_EXCEPTION);

        String key = "cart::" + authentication.getName();

        List<Long> cartList = cartRedisService.cartList(key);

        for (OrderRequestDto order : list) {
            if (cartList.contains(order.getProductId())) orderService.orderProduct(order.getProductId(), order.getOrderNum(), authentication);
            else throw new RequestException(NOT_FOUND_EXCEPTION);
        }
    }
}
