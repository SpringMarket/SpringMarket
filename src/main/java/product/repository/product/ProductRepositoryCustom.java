package product.repository.product;

import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    //Page<Product> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword);
    Page<ProductMainResponseDto> mainFilter(Pageable pageable, Long category, String stock, Long minPrice, Long maxPrice, String keyword, String sort);
    ProductDetailResponseDto detail(Long productId);
    ProductMainResponseDto detailMain(Long productId);
    void addView(Long productId, int viewCnt);
    Integer getView(Long productId);
}
