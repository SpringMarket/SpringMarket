package product.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import product.entity.product.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
