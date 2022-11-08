package product.repository.product;

import product.entity.product.Order;
import product.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findByUser(User user);
}
