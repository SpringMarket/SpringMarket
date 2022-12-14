package product.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import product.dto.order.MyPageResponseDto;
import product.entity.order.Orders;
import product.entity.product.Product;
import product.entity.user.User;
import product.exception.RequestException;
import product.repository.order.OrderRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if(orderNum < 1) throw new RequestException(ORDER_NUM_EXCEPTION);

        Product productTest = productRepository.findByProductId(productId);
        if( productTest == null ) throw new RequestException(NOT_FOUND_EXCEPTION);

        User user = getUser(authentication);

        Product product = productModify(productId, orderNum, user.getAge());


        // 주문 데이터 저장
        Orders order = new Orders(orderNum, "배송중", product, user);
        orderRepository.save(order);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Product productModify(Long productId, Long orderNum, String age){

        Product product = productRepository.findByIdWithPessimisticLock(productId);

        // 재고 부족 예외처리
        if(product.getStock()< orderNum) throw new RequestException(OUT_OF_STOCK_EXCEPTION);

        // 상품 재고 차감
        product.orderChangeStock(orderNum);
        // 상품 정보 변경
        product.getProductInfo().plusPreference(orderNum, age);

        productRepository.saveAndFlush(product);

        return product;
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<MyPageResponseDto> myPage(Pageable pageable, Authentication authentication) {

        User user = getUser(authentication);

        return orderRepository.orderFilter(user,pageable);
    }

    // 주문 취소
    @Transactional
    public void cancel(Authentication authentication, Long orderId) {

        User user = getUser(authentication);

        Orders order = orderRepository.findByOrderId(orderId);
        if (order == null) throw new RequestException(NOT_FOUND_EXCEPTION);

        checkCancelValid(user, order);

        // 재고 수 변경
        order.getProduct().cancelChangeStock(order.getOrderNum());

        // 주문 취소로 status 변경
        order.cancel();

        // productInfo 변경
        order.getProduct().getProductInfo().minusPreference(order.getOrderNum(),user.getAge());
    }

    private User getUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
    }

    public void checkCancelValid(User user, Orders order){
        // 로그인된 사용자와 주문 테이블에 저장된 사용자 일치여부 조회
        if (!user.equals(order.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        // 주문 상태에 따른 주문 취소 실패 처리
        if (order.getOrderStatus().equals("배송완료") || order.getOrderStatus().equals("주문취소")) {
            throw new RequestException(ORDER_FINISH_EXCEPTION);
        }
    }
}
