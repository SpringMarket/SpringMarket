package product.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import product.entity.product.View;

public interface ViewRepository extends JpaRepository<View, Long> {
}
