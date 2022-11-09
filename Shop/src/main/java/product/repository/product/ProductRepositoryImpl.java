package product.repository.product;

import com.amazonaws.util.StringUtils;
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

    // CandyResponseDto와 성능 비교 (content 차이)
    @Override
    public Page<ProductResponseDetailDto> mainFilter(Pageable pageable, String category, Boolean stock,
                                                     Long minPrice, Long maxPrice, String keyword, String sorting) {

        QProduct qProduct = QProduct.product;
        // 데이터 수 줄여서 조회 테스트
        // 커버링 인덱스 테스트

        List<ProductResponseDetailDto> result = queryFactory.from(qProduct)
//                .select(new QProductResponseDetailDto(qProduct))
                .select(Projections.constructor(ProductResponseDetailDto.class,qProduct))
                .where(categoryFilter(category),
                        (isStock(stock)),
                        minPriceRange(minPrice),
                        maxPriceRange(maxPrice),
                        qProduct.title.contains(keyword)) // like("%" + str + "%")
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(sorting(sorting))
                .fetch();

        return new PageImpl<>(result,pageable,result.size());
    }

    private BooleanExpression categoryFilter(String category) {
        if (StringUtils.isNullOrEmpty(category)) return null;
        return QProduct.product.category.category.eq(category);
    }

    private BooleanExpression isStock(Boolean stock) {
        if (!stock) return null;
        return QProduct.product.productInfo.stock.ne(0L);
    }

    private BooleanExpression minPriceRange(Long minPrice) {
        if (minPrice != null) QProduct.product.price.gt(minPrice);
        return null;
    }

    private BooleanExpression maxPriceRange(Long maxPrice) {
        if (maxPrice != null) QProduct.product.price.lt(maxPrice);
        return null;
    }

    private OrderSpecifier<?> sorting(String sorting) {
        switch (sorting) {
            case "조회순":
                return QProduct.product.productInfo.view.desc();
            case "날짜순":
                return QProduct.product.createdTime.desc();
            case "10대":
                return QProduct.product.productInfo.ten.desc();
            case "20대":
                return QProduct.product.productInfo.twenty.desc();
            case "30대":
                return QProduct.product.productInfo.thirty.desc();
            case "40대 이상":
                return QProduct.product.productInfo.forty.desc();
        }
        return null;
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
    public List<Product> warmup(Long categoryId) {
        QProduct qProduct = QProduct.product;
        return queryFactory.selectFrom(qProduct)
                .where(qProduct.category.categoryId.eq(categoryId))
                .orderBy(qProduct.productInfo.view.desc())
                .limit(60)
                .fetch();
    }
}
