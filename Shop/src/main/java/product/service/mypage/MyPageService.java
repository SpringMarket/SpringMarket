package product.service.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.mypage.MyPageResponseDto;
import product.entity.product.Order;
import product.entity.product.Product;
import product.entity.user.User;
import product.exception.RequestException;
import product.repository.product.OrderRepository;
import product.repository.product.ProductRepository;

import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;
import static product.exception.ExceptionType.ORDER_FINISH_EXCEPTION;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<MyPageResponseDto> myPage(Pageable pageable, User user) {

        /*List<Order> orders = orderRepository.findByUser(user);
        List<MyPageResponseDto> myPageResponseDtos = new ArrayList<>();
        orders.stream().forEach(i -> myPageResponseDtos.add(MyPageResponseDto.toDto(i)));*/

        return orderRepository.orderFilter(user,pageable);
    }

    // 주문 취소
    @Transactional(readOnly = true)
    public void cancel(User user, Order order) {

        if (order.getOrderStatus().equals("배송완료")) {
            throw new RequestException(ORDER_FINISH_EXCEPTION);
        }

        Product product = productRepository.findByProductId(order.getProduct().getProductId())
                .orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));



        product.cancel(order.getOrderNum());

        order.cancel();
    }
}
