package product.controller.product;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @GetMapping("/rank/list/{categoryId}")
    public Response getRankingList(@PathVariable Long categoryId) {
        return success(productService.getRankingList(categoryId));
    }


    // orderBy first(JPA) or last(Query DSL) 성능테스트
    // 메인 페이지
    @GetMapping("/products")
    public Response findAllProduct(@PageableDefault(size = 15) Pageable pageable,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "stock", required = false) String stock,
                                   @RequestParam(value = "minPrice", required = false) Long minPrice,
                                   @RequestParam(value = "maxPrice", required = false) Long maxPrice,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "sorting", required = false) String sorting
    )
    {
        log.info("category: "+category+ " stock: "+stock+" minPrice: "+minPrice+" maxPrice: "+maxPrice+" keyword: "+keyword+" sorting: "+sorting);
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.by("view").descending());
        if(sorting.equals("날짜순")){
            pageable1 = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.by("createdTime").descending());
        }
        return success(productService.findAllProduct(pageable1, category, stock, minPrice, maxPrice, keyword, sorting));
    }

    // 상세 페이지
    @GetMapping("/products/{id}")
    public Response findProduct(@PathVariable Long id) {
        // productService.countView(id);
        log.info(id+"번 상품 조회");
        return success(productService.findProduct(id));
    }
}
