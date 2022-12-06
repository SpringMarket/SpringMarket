package product.controller.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import product.dto.order.OrderRequestDto;
import product.response.Response;
import product.service.cart.CartService;

import java.util.List;

import static product.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    // 카트 조회
    @GetMapping("/cart/list")
    public Response showCart(@PageableDefault(size = 10) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return success(cartService.showCart(authentication, pageable));
    }


    // 카트에 추가
    @GetMapping("/cart/{productId}")
    public Response addCart(@PathVariable Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        cartService.addCart(productId, authentication);
        return success();
    }

    // 카트에서 삭제
    @DeleteMapping("/cart/{productId}")
    public Response deleteCart(@PathVariable Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        cartService.deleteCart(productId, authentication);
        return success();
    }

    // 카트 주문
    @PostMapping("/cart/order")
    public Response orderCart(@RequestBody List<OrderRequestDto> list) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        cartService.orderCart(authentication, list);
        return success();
    }
}
