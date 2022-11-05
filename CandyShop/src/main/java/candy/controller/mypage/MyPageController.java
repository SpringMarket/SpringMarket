package candy.controller.mypage;


import candy.entity.candy.Order;
import candy.entity.user.User;
import candy.exception.RequestException;
import candy.repository.candy.OrderRepository;
import candy.repository.user.UserRepository;
import candy.response.Response;
import candy.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static candy.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static candy.exception.ExceptionType.NOT_FOUND_EXCEPTION;
import static candy.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // 주문 목록 조회
    @GetMapping("/mypage")
    public Response myPage(@PageableDefault(size = 10) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(myPageService.myPage(pageable, user));
    }

    // 주문 취소
    @PatchMapping("/mypage/{orderId}")
    public Response cancel(@PathVariable Long orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        if (!user.equals(order.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }

        myPageService.cancel(user, order);

        return success();
    }
}
