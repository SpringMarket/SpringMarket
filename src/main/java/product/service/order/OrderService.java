package product.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.order.MyPageResponseDto;
import product.dto.order.OrderRequestDto;
import product.dto.product.ProductResponseDetailDto;
import product.entity.order.Order;
import product.entity.product.Product;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.order.OrderRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;
import product.service.RedisService;

import java.util.ArrayList;
import java.util.List;

import static product.exception.ExceptionType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // 상품 주문
    @Transactional
    public void orderProduct(Long productId, Long orderNum, Authentication authentication) {

        log.info("Order Start....");

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        Product product = productRepository.findByProductId(productId);
        if (product == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        // 재고 부족 예외처리
        if(product.getStock().getStock()< orderNum) throw new RequestException(OUT_OF_STOCK_EXCEPTION);

        // 상품 재고 차감
        product.getStock().order(orderNum);

        // 상품 정보 변경
        product.getProductInfo().PlusPreference(orderNum,user.getAge());

        // 주문 데이터 저장
        orderRepository.save(new Order(product,orderNum, user));
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<MyPageResponseDto> myPage(Pageable pageable, Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        return orderRepository.orderFilter(user,pageable);
    }

    // 주문 취소
    @Transactional
    public void cancel(Authentication authentication, Long orderId) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        Order order = orderRepository.findByOrderId(orderId);
        if (order == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        // 로그인된 사용자와 주문 테이블에 저장된 사용자 일치여부 조회
        if (!user.equals(order.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }

        // 주문 상태에 따른 주문 취소 실패 처리
        if (order.getOrderStatus().equals("배송완료") || order.getOrderStatus().equals("주문취소")) {
            throw new RequestException(ORDER_FINISH_EXCEPTION);
        }

        // 재고 수 변경
        order.getProduct().getStock().cancel(order.getOrderNum());

        // 주문 취소로 status 변경
        order.cancel();

        // productInfo 변경
        order.getProduct().getProductInfo().MinusPreference(order.getOrderNum(),user.getAge());
    }

}
