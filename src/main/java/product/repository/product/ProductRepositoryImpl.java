package product.repository.product;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import product.dto.product.ProductResponseDetailDto;
import product.entity.product.Product;
import product.entity.product.QProduct;
import product.entity.product.QView;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QProduct qProduct = QProduct.product;

    QView qView = QView.view1;

    @Override
    public Page<ProductResponseDetailDto> mainFilter(Pageable pageable, String category, String stock,
                                                     Long minPrice, Long maxPrice, String keyword, String sorting) {

        // 데이터 수 줄여서 조회 테스트
        // 커버링 인덱스 테스트
        List<ProductResponseDetailDto> result = queryFactory.from(qProduct)
                .select(Projections.constructor(ProductResponseDetailDto.class, qProduct))
                .where(categoryFilter(category),
                        isStock(stock),
                        maxPriceRange(maxPrice),
                        minPriceRange(minPrice),
                        keywordContain(keyword))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(sorting(sorting))
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }
    @Override
    public Product detail(Long productId) {
        return queryFactory.selectFrom(qProduct)
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }
    @Override
    public List<Product> warmup(Long categoryId) {
        return queryFactory.selectFrom(qProduct)
                .where(qProduct.category.categoryId.eq(categoryId))
                .orderBy(qProduct.view.view.desc())
                .limit(60)
                .fetch();
    }
    @Override
    public void addView(Long productId, int viewCnt) {
        queryFactory
                .update(qView)
                .set(qView.view, viewCnt)
                .where(qView.view_id.eq(productId))
                .execute();
    }
    @Override
    public Integer getView(Long productId) {
        return queryFactory.select(qProduct.view.view)
                .from(qProduct)
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }

    // 카테고리 null -> Default "아우터" return
    // if (StringUtils.isNullOrEmpty(category)) QProduct.product.category.category.eq("아우터");
    private BooleanExpression categoryFilter(String category) {
        if (StringUtils.isNullOrEmpty(category)) return null;
        return QProduct.product.category.category.eq(category);
    }

    private BooleanExpression isStock(String stock) {
        if (StringUtils.isNullOrEmpty(stock)) return null;
        if (stock.equals("1")) return null; // "1" : 품절상품 포함 -> null 리턴
        return QProduct.product.stock.stock.ne(0L); // "1" : 품절상품 미포함 -> stock != 0 리턴
    }

    private BooleanExpression minPriceRange(Long minPrice) {
        if (minPrice != null) return QProduct.product.price.goe(minPrice); // Price >= minPrice
        return null;
    }

    private BooleanExpression maxPriceRange(Long maxPrice) {
        if (maxPrice != null) return QProduct.product.price.loe(maxPrice); // Price <= maxPrice
        return null;
    }

    private BooleanExpression keywordContain(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) return null;
        return QProduct.product.title.contains(keyword);
    }

    private OrderSpecifier<?> sorting(String sorting) {

        if (StringUtils.isNullOrEmpty(sorting)) return QProduct.product.view.view.desc();

        switch (sorting) {
            case "조회순":
                return QProduct.product.view.view.desc();
            case "날짜순":
                return QProduct.product.createdTime.desc();
            case "10대":
                return QProduct.product.productInfo.ten.desc();
            case "20대":
                return QProduct.product.productInfo.twenty.desc();
            case "30대":
                return QProduct.product.productInfo.thirty.desc();
            case "40대 이상":
                return QProduct.product.productInfo.over_forty.desc();
        }
        return QProduct.product.view.view.desc();
    }
}
