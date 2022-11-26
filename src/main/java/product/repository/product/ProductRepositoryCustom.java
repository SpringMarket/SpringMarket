package product.repository.product;

import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    //Page<Product> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword);
    Page<ProductMainResponseDto> mainFilter(Pageable pageable, String category, String stock, Long minPrice, Long maxPrice, String keyword, String sort);
    ProductDetailResponseDto detail(Long productId);
    ProductMainResponseDto detail_2(Long productId);
    List<ProductDetailResponseDto> warmupDetail(Long categoryId);
    List<Product> warmup(Long categoryId);
    void addView(Long productId, int viewCnt);
    Integer getView(Long productId);
}
