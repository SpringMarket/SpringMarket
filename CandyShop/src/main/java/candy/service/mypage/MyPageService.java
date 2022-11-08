package candy.service.mypage;

import candy.dto.mypage.MyPageResponseDto;
import candy.entity.candy.Candy;
import candy.entity.candy.Order;
import candy.entity.user.User;
import candy.exception.RequestException;
import candy.repository.candy.CandyRepository;
import candy.repository.candy.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static candy.exception.ExceptionType.NOT_FOUND_EXCEPTION;
import static candy.exception.ExceptionType.ORDER_FINISH_EXCEPTION;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final CandyRepository candyRepository;
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

        Candy candy = candyRepository.findById(order.getCandy().getId())
                .orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));



        candy.cancel(order.getOrderNum());

        order.cancel();
    }
}
