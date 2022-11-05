package candy.repository.candy;

import candy.entity.candy.Candy;
import candy.entity.candy.QCandy;
import candy.entity.candy.QOrder;
import candy.entity.user.QUser;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CandyRepositoryImpl implements CandyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Candy> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword) {


        QOrder qOrder = QOrder.order;
        QCandy qCandy = QCandy.candy;

        QueryResults<Candy> result = queryFactory.select(qCandy).from(qCandy)
                .innerJoin(qOrder).on(qCandy.id.eq(qOrder.candy.id))
                .where(qCandy.category.category.eq(category))
                //.where(stock, qOrder.candy.stock.ne(0))
                .where(qCandy.price.between(price.get(0),price.get(1)))
                .where(qOrder.user.age.eq(age))
                .where(qCandy.title.contains(keyword)) // like("%" + str + "%")
                //.or(qOrder.candy.content.contains(keyword)))
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(qCandy.view.desc())
                .fetchResults();

        return new PageImpl<>(result.getResults(),pageable,result.getTotal());

    }
}
