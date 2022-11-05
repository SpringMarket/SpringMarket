package candy.repository.candy;

import candy.dto.candy.CandyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {
    Page<CandyResponseDto> orderFilter(String category, Long id);
}
