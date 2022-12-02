package product.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import product.entity.product.Product;

import javax.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Product findByProductId(Long productId);

    Product findByTitle(String title);
}

