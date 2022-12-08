package product.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import product.entity.product.Product;

import javax.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {


    Product findByProductId(Long productId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :productId")
    Product findByIdWithPessimisticLock(@Param("productId") Long productId);
}

