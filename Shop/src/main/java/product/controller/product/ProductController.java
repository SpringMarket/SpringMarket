package product.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import product.dto.product.ProductCreateDto;
import product.response.Response;
import product.service.product.ProductService;

import static product.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    // Warm UP
    @GetMapping("/warmup")
    public Response warmup() {
        productService.warmup();
        return success("Start ^__^ !!");
    }

    // 메인 페이지
    // orderBy first(JPA) or last(Query DSL) 성능테스트
    @GetMapping("/products")
    public Response findAllProduct(@PageableDefault(size = 20) Pageable pageable,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "stock", required = false) Boolean stock,
                                   @RequestParam(value = "minPrice", required = false) Long minPrice,
                                   @RequestParam(value = "maxPrice", required = false) Long maxPrice,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "sorting", required = false) String sorting
    )
    {
        return success(productService.findAllProduct(pageable, category, stock, minPrice, maxPrice, keyword, sorting));
    }

    // 상세 페이지
    @GetMapping("/products/{id}")
    public Response findProduct(@PathVariable Long id) {
        productService.countView(id);
        return success(productService.findProduct(id));
    }

    // 상품 데이터 생성 :: 더미
    @PostMapping("/create")
    public Response createProduct(@RequestBody ProductCreateDto productCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        productService.create(productCreateDto, authentication);
        return success();
    }

}
