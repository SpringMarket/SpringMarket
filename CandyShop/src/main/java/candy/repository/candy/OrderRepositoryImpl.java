package candy.repository.candy;

import candy.dto.candy.CandyResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CandyResponseDto> orderFilter(String category, Long id) {
        return null;
    }
}
