package product.service.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.mypage.MyPageResponseDto;
import product.entity.product.Order;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.product.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;

import static product.exception.ExceptionType.*;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<MyPageResponseDto> myPage(Pageable pageable, Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return orderRepository.orderFilter(user,pageable);
    }

    // 주문 취소
    @Transactional
    public void cancel(Authentication authentication, Long orderId) {

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        if (!user.equals(order.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }

        if (order.getOrderStatus().equals("배송완료")) {
            throw new RequestException(ORDER_FINISH_EXCEPTION);
        }

        Product product = productRepository.findByProductId(order.getProduct().getProductId()).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));
        product.getProductInfo().cancel(order.getOrderNum());

        // 주문 취소로 status 변경
        order.cancel();
    }
}
