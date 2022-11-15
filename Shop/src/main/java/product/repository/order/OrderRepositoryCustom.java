package product.repository.order;

import product.dto.order.MyPageResponseDto;
import product.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<MyPageResponseDto> orderFilter(User user, Pageable pageable);

}
