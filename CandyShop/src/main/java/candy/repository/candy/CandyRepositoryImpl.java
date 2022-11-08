package candy.repository.candy;

import candy.dto.candy.CandyResponseDetailDto;
import candy.entity.candy.Candy;
import candy.entity.candy.QCandy;
import candy.entity.candy.QOrder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    private QOrder qOrder;

    /*@Override
    public Page<Candy> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword) {


        QOrder qOrder = QOrder.order;
        QCandy qCandy = QCandy.candy;

        *//*BooleanBuilder builder = new BooleanBuilder();
        if (stock) {
            builder.and(qCandy.category.category.eq(category));
        }*//*

        List<Candy> result = queryFactory.select(qCandy).from(qCandy)
                .innerJoin(qOrder).on(qCandy.id.eq(qOrder.candy.id))
                .where(qCandy.category.category.eq(category))
                .where(isStock(stock))
                .where(qCandy.price.between(price.get(0),price.get(1)))
                .where(qOrder.user.age.eq(age))
                .where(qCandy.title.contains(keyword)) // like("%" + str + "%")
                //.or(qOrder.candy.content.contains(keyword)))
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(qCandy.view.desc())
                .fetch();

        return new PageImpl<>(result,pageable,result.size());
    }*/

    // CandyResponseDto와 성능 비교 (content 차이)
    @Override
    public Page<CandyResponseDetailDto> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword) {

        QOrder qOrder = QOrder.order;
        QCandy qCandy = QCandy.candy;

        List<CandyResponseDetailDto> result = queryFactory.from(qCandy)
                .select(Projections.fields(CandyResponseDetailDto.class,
                        qCandy.id, qCandy.title, qCandy.content, qCandy.photo, qCandy.price, qCandy.stock,qCandy.view,qCandy.category.id))
                .innerJoin(qOrder).on(qCandy.id.eq(qOrder.candy.id))
                .where(qCandy.category.category.eq(category))
                .where(isStock(stock))
                .where(qCandy.price.between(price.get(0),price.get(1)))
                .where(qOrder.user.age.eq(age))
                .where(qCandy.title.contains(keyword)) // like("%" + str + "%")
                //.or(qOrder.candy.content.contains(keyword)))
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(qCandy.view.desc())
                .fetch();

        return new PageImpl<>(result,pageable,result.size());
    }


    @Override
    public Candy detail(Long categoryId, Long candyId) {
        QCandy qCandy = QCandy.candy;
        return queryFactory.selectFrom(qCandy)
                .where(qCandy.category.id.eq(categoryId))
                .where(qCandy.id.eq(candyId))
                .fetchOne();
    }

    @Override
    public List<Candy> warmup() {
        QCandy qCandy = QCandy.candy;
        return queryFactory.selectFrom(qCandy)
                .groupBy(qCandy.category)
                .orderBy(qCandy.view.desc()) // 다시 확인
                .limit(60)
                .fetch();
    }


    private BooleanExpression isStock(Boolean stock) {
        if (stock) {
            return null;
        }
        return qOrder.candy.stock.ne(0L);
    }
}
