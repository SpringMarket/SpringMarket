package product.controller.product;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import product.response.Response;
import product.service.product.ProductService;

import static product.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;
    Logger log = LoggerFactory.getLogger("ACCESS");

    // 랭킹보드 조회
    @GetMapping("/rank/list/{categoryId}/{preferId}")
    public Response getRankingList(@PathVariable Long categoryId, @PathVariable Long preferId) {
        return success(productService.getRankingList(categoryId, preferId));
    }


    // orderBy first(JPA) or last(Query DSL) 성능테스트
    // 메인 페이지
    @GetMapping("/products")
    public Response findAllProduct(@PageableDefault(size = 15) Pageable pageable,
                                   @RequestParam(value = "categoryId", required = false) Long categoryId,
                                   @RequestParam(value = "stock", required = false) String stock,
                                   @RequestParam(value = "minPrice", required = false) Long minPrice,
                                   @RequestParam(value = "maxPrice", required = false) Long maxPrice,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "sorting", required = false) String sorting
    )
    {
        log.info("categoryId: "+categoryId+ " stock: "+stock+" minPrice: "+minPrice+" maxPrice: "+maxPrice+" keyword: "+keyword+" sorting: "+sorting);

        return success(productService.findAllProduct(pageable, categoryId, stock, minPrice, maxPrice, keyword, sorting));
    }

    // 메인 페이지 키워드 조회
    @GetMapping("/products/keyword")
    public Response findAllProduct(@PageableDefault(size = 15) Pageable pageable,
                                   @RequestParam(value = "keyword", required = false) String keyword) {
        return success(productService.findByKeyword(pageable,keyword));
    }

    // 상세 페이지
    @GetMapping("/products/{id}")
    public Response findProduct(@PathVariable Long id) {
        // productService.countView(id);
        log.info(id+"번 상품 조회");
        return success(productService.findProduct(id));
    }
}
