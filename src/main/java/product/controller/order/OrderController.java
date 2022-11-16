package product.controller.order;


import product.aop.annotation.Auth;
import product.dto.order.OrderRequestDto;
import product.response.Response;
import product.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import static product.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class OrderController {

    private final OrderService orderService;

    // 상품 주문
    @Auth
    @PostMapping("/order")
    public Response order(@RequestBody OrderRequestDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        orderService.orderProduct(dto.getProductId(), dto.getOrderNum(), authentication);

        return success();
    }

    // 주문 목록 조회
    @GetMapping("/list")
    public Response myPage(@PageableDefault(size = 10) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return success(orderService.myPage(pageable, authentication));
    }

    // 주문 취소
    @PatchMapping("/{orderId}")
    public Response cancel(@PathVariable Long orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        orderService.cancel(authentication, orderId);
        return success();
    }
}
