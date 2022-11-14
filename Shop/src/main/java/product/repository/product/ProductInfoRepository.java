package product.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import product.entity.product.ProductInfo;

public interface ProductInfoRepository extends JpaRepository <ProductInfo, Long> {

    ProductInfo findByProductId(Long productId);
}
