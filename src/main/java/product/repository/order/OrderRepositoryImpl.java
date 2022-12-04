package product.repository.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import product.dto.order.MyPageResponseDto;
import product.entity.order.QOrders;
import product.entity.user.User;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<MyPageResponseDto> orderFilter(User user, Pageable pageable) {
        QOrders qOrder = QOrders.orders;
        List<MyPageResponseDto> result =  queryFactory.from(qOrder)
                .select(Projections.constructor(MyPageResponseDto.class, qOrder))
                .where(qOrder.user.eq(user))
                .where(qOrder.orderStatus.ne("주문취소"))
                .orderBy(qOrder.orderTime.desc())
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(result,pageable,result.size());
    }
}
