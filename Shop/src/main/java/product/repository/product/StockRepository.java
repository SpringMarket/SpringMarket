package product.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import product.entity.product.Stock;

public interface StockRepository extends JpaRepository<Stock,Long> {
    Stock findByProductId(Long id);
}
