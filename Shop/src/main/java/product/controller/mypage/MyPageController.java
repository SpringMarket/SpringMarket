package product.controller.mypage;


import product.entity.product.Order;
import product.entity.user.User;
import product.exception.RequestException;
import product.repository.product.OrderRepository;
import product.repository.user.UserRepository;
import product.response.Response;
import product.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static product.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;
import static product.response.Response.success;

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

        return success(myPageService.myPage(pageable, authentication));
    }

    // 주문 취소
    @PatchMapping("/mypage/{orderId}")
    public Response cancel(@PathVariable Long orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        myPageService.cancel(authentication, orderId);
        return success();
    }
}
