package product.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.user.UserRepository;
import product.response.Response;
import product.service.product.ProductService;

import java.util.List;

import static product.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;
    private final UserRepository userRepository;


    // Warm UP
    @GetMapping("/candycandy")
    public Response warmup() {

        productService.warmup();

        return success("Start ^__^ !!");
    }

    // 메인 페이지
    // orderBy first(JPA) or last(Query DSL) 성능테스트
    @GetMapping("/product")
    public Response findAllProduct(@PageableDefault(size = 20) Pageable pageable,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "stock", required = false) Boolean stock,
                                 @RequestParam(value = "minPrice", required = false) Long minPrice,
                                 @RequestParam(value = "maxPrice", required = false) Long maxPrice,
                                 @RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "sorting", required = false) String sorting
                                 ) {

        return success(productService.findAllProduct(pageable, category, stock, minPrice, maxPrice, keyword, sorting));
    }

    // 상세 페이지
    @GetMapping("/product/{id}")
    public Response findProduct(@PathVariable Long id) {

        return success(productService.findProduct(id));
    }

    // 상품 주문
    @PostMapping("/product/{id}/order")
    public Response order(@PathVariable Long id, @RequestBody Long orderNum) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        productService.orderProduct(id, orderNum, authentication);

        return success();
    }


}
