package product.repository.product;

import com.querydsl.core.types.OrderSpecifier;
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
    private QProduct qProduct;


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
    public Page<ProductResponseDetailDto> mainFilter(Pageable pageable, String category, Boolean stock,
                                                     List<Long> price, String age, String keyword, String sorting) {

        QOrder qOrder = QOrder.order;
        QProduct qProduct = QProduct.product;
        // 데이터 수 줄여서 조회 테스트
        // 커버링 인덱스 테스트

        List<ProductResponseDetailDto> result = queryFactory.from(qOrder)
//                .select(Projections.fields(ProductResponseDetailDto.class,
//                        qProduct.id, qProduct.title, qProduct.content, qProduct.photo,
//                        qProduct.price, qProduct.stock,qProduct.view,qProduct.category.id))
//                .select(new QProductResponseDetailDto(qProduct))
                .select(Projections.constructor(ProductResponseDetailDto.class,qProduct.productId, qProduct.title, qProduct.content, qProduct.photo,
                       qProduct.price, qProduct.stock,qProduct.view,qProduct.createdTime))
                .leftJoin(qOrder.product,qProduct)
                //.where(qProduct.category.category.eq(category))
                .where(isStock(stock))
                //.where(qOrder.user.age.eq(age))
                .where(qProduct.price.between(price.get(0),price.get(1)))
                .where(qProduct.title.contains(keyword)) // like("%" + str + "%")
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(sorting(sorting))
                .fetch();


        return new PageImpl<>(result,pageable,result.size());
    }


    @Override
    public Product detail(Long categoryId, Long productId) {
        QProduct qProduct = QProduct.product;
        return queryFactory.selectFrom(qProduct)
                .where(qProduct.category.categoryId.eq(categoryId))
                .where(qProduct.productId.eq(productId))
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
        if (!stock) {
            return null;
        }
        return qProduct.stock.ne(0L);
    }

    private OrderSpecifier<?> sorting(String sorting) {
        if (sorting.equals("조회순")) return QProduct.product.view.desc();
        else if (sorting.equals("날짜순")) return QProduct.product.createdTime.desc();
        return null;
    }
}
