package candy.repository.candy;

import candy.dto.mypage.MyPageResponseDto;
import candy.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<MyPageResponseDto> orderFilter(User user, Pageable pageable);
}
