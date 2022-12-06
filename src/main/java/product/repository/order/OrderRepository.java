package product.repository.order;

import product.entity.order.Orders;
import product.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long>, OrderRepositoryCustom {
    Orders findByOrderId(Long orderId);
}
