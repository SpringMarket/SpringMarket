package product.controller.cart;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    // 카트 조회
    @GetMapping("/show")
    public Response showCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return success(cartService.showCart(authentication));
    }

    // 카트에 추가
    @GetMapping("/add/{productId}")
    public Response addCart(@PathVariable Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return success(cartService.addCart(productId, authentication));
    }

    // 카트에서 삭제
    @DeleteMapping("/delete/{productId}")
    public Response deleteCart(@PathVariable Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return success(cartService.deleteCart(productId, authentication));
    }

    // 카트 주문
    @PostMapping("/order")
    public Response orderCart(@RequestBody List<OrderRequestDto> list) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return success(cartService.orderCart(authentication, list));
    }
}