package product.repository.product;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import product.dto.product.ProductResponseDetailDto;
import product.entity.product.Product;
import product.entity.product.QOrder;
import product.entity.product.QProduct;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
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
    public Page<ProductResponseDetailDto> mainFilter(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword) {

        QOrder qOrder = QOrder.order;
        QProduct qProduct = QProduct.product;

        List<ProductResponseDetailDto> result = queryFactory.from(qProduct)
                .select(Projections.fields(ProductResponseDetailDto.class,
                        qProduct.id, qProduct.title, qProduct.content, qProduct.photo, qProduct.price, qProduct.stock,qProduct.view,qProduct.category.id))
                .innerJoin(qOrder).on(qProduct.id.eq(qOrder.product.id))
                .where(qProduct.category.category.eq(category))
                .where(isStock(stock))
                .where(qProduct.price.between(price.get(0),price.get(1)))
                .where(qOrder.user.age.eq(age))
                .where(qProduct.title.contains(keyword)) // like("%" + str + "%")
                //.or(qOrder.candy.content.contains(keyword)))
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(qProduct.view.desc())
                .fetch();

        return new PageImpl<>(result,pageable,result.size());
    }


    @Override
    public Product detail(Long categoryId, Long productId) {
        QProduct qProduct = QProduct.product;
        return queryFactory.selectFrom(qProduct)
                .where(qProduct.category.id.eq(categoryId))
                .where(qProduct.id.eq(productId))
                .fetchOne();
    }

    @Override
    public List<Product> warmup() {
        QProduct qProduct = QProduct.product;
        return queryFactory.selectFrom(qProduct)
                .groupBy(qProduct.category)
                .orderBy(qProduct.view.desc()) // 다시 확인
                .limit(60)
                .fetch();
    }


    private BooleanExpression isStock(Boolean stock) {
        if (stock) {
            return null;
        }
        return qOrder.product.stock.ne(0L);
    }
}
