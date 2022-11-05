package candy.repository.candy;

import candy.entity.candy.Candy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CandyRepositoryCustom {
    Page<Candy> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword);
}
