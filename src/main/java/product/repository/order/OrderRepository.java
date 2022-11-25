package product.repository.order;

import product.entity.order.Order;
import product.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Order findByOrderId(Long orderId);
}
