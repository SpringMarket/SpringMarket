package product.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.order.MyPageResponseDto;
import product.entity.order.Order;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.product.Stock;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.order.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.product.StockRepository;
import product.repository.user.UserRepository;

import static product.exception.ExceptionType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final ProductInfoRepository productInfoRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    // 상품 주문
    @Transactional
    public void orderProduct(Long productId, Long orderNum, Authentication authentication) {

        log.info("Order Start....");

        User user = userRepository.findByEmail(authentication.getName());
        //        .orElseThrow(() -> new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION));
        if(user == null) throw new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION);

        Product product = productRepository.findByProductId(productId);
        if (product == null) throw new RequestException(ExceptionType.NOT_FOUND_EXCEPTION);

        if(product.getStock().getStock()< orderNum) throw new RequestException(ExceptionType.OUT_OF_STOCK_EXCEPTION);

        // 재고 수 추가
        product.getStock().order(orderNum);

        Order order = new Order(product,orderNum, user);
        orderRepository.save(order);

        // productInfo 변경
        product.getProductInfo().order(orderNum,user.getAge());

    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<MyPageResponseDto> myPage(Pageable pageable, Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName());
        if(user == null) throw new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION);

        return orderRepository.orderFilter(user,pageable);
    }

    // 주문 취소
    @Transactional
    public void cancel(Authentication authentication, Long orderId) {

        User user = userRepository.findByEmail(authentication.getName());
        if(user == null) throw new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION);

        Order order = orderRepository.findByOrderId(orderId);
        if (order == null) throw new RequestException(ExceptionType.NOT_FOUND_EXCEPTION);

        if (!user.equals(order.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }

        if (order.getOrderStatus().equals("배송완료") || order.getOrderStatus().equals("주문취소")) {
            throw new RequestException(ORDER_FINISH_EXCEPTION);
        }
        Product product = productRepository.findByProductId(order.getProduct().getProductId());
        if (product == null) throw new RequestException(ExceptionType.NOT_FOUND_EXCEPTION);


        if (product.getStock().getStock() == null) throw new RequestException(ExceptionType.NOT_FOUND_EXCEPTION);

        // 재고 수 변경
        product.getStock().cancel(order.getOrderNum());

        // 주문 취소로 status 변경
        order.cancel();

        // productInfo 변경
        product.getProductInfo().order(order.getOrderNum(),user.getAge());
    }
}
