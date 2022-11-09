package product.repository.product;

import product.dto.product.ProductResponseDetailDto;
import product.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    //Page<Product> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword);
    Page<ProductResponseDetailDto> mainFilter(Pageable pageable, String category, Boolean stock, Long minPrice, Long maxPrice, String age, String keyword, String sort);
    Product detail(Long categoryId, Long productId);
    List<Product> warmup();
}
