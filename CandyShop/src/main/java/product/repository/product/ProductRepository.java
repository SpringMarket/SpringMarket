package product.repository.product;

import product.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findById(Long id);

}
