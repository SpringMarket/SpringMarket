package product.repository.product;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.*;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QProduct qProduct = QProduct.product;
    QCategory qCategory = QCategory.category1;
    QProductInfo qProductInfo = QProductInfo.productInfo;


    // 필터링 조회
    @Override
    public Page<ProductMainResponseDto> mainFilter(Pageable pageable, String category, String stock,
                                                   Long minPrice, Long maxPrice, String keyword, String sorting) {

        // 커버링 인덱스
       List<Long> ids = queryFactory.from(qProduct)
               .select(qProduct.productId)
               .innerJoin(qProduct.category,qCategory)
               .innerJoin(qProduct.productInfo, qProductInfo)
               .where(categoryFilter(category),
                        isStock(stock),
                        minPriceRange(minPrice),
                        maxPriceRange(maxPrice),
                        keywordMatch(keyword))
               .orderBy(sorting(sorting))
               .limit(pageable.getPageSize())
               .offset(pageable.getOffset())
               .fetch();

       // Null -> 공백 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        // 메인 쿼리
        List<ProductMainResponseDto> result = queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct))
                //.innerJoin(qProduct.category,qCategory)
                .innerJoin(qProduct.productInfo, qProductInfo)
                .where(qProduct.productId.in(ids))
                .orderBy(sorting(sorting))
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    } // -> 종료



    // 상세 조회 -> ProductDetailResponseDto
    @Override
    public ProductDetailResponseDto detail(Long productId) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductDetailResponseDto.class, qProduct))
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }

    // 상세 조회 -> ProductMainResponseDto
    @Override
    public ProductMainResponseDto detail_2(Long productId) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct.productId,qProduct.title,qProduct.photo,qProduct.price))
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }

    // WarmUp -> Return ProductDetailResponseDto Category Top 100
    @Override
    public List<ProductDetailResponseDto> warmupDetail(Long categoryId) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductDetailResponseDto.class, qProduct))
                .where(qProduct.category.categoryId.eq(categoryId))
                .orderBy(qProduct.view.desc())
                .limit(100)
                .fetch();
    }

    // WarmUp -> Return ProductDetailResponseDto Category Top 100
    @Override
    public List<ProductRankResponseDto> warmupMain(Long categoryId) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductRankResponseDto.class, qProduct))
                .where(qProduct.category.categoryId.eq(categoryId))
                .orderBy(qProduct.view.desc())
                .limit(100)
                .fetch();
    }

    // WarmUp -> Return Product Category Top 100
    @Override
    public List<Product> warmup(Long categoryId) {
        return queryFactory.from(qProduct)
                .select(qProduct)
                .where(qProduct.category.categoryId.eq(categoryId))
                .orderBy(qProduct.view.desc())
                .limit(100)
                .fetch();
    }

    // 조회수 Update Query
    @Override
    public void addView(Long productId, int viewCnt) {
        queryFactory
                .update(qProduct)
                .set(qProduct.view, viewCnt)
                .where(qProduct.productId.eq(productId))
                .execute();
    }

    // 상품 조회수 Return
    @Override
    public Integer getView(Long productId) {
        return queryFactory.select(qProduct.view)
                .from(qProduct)
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }

    // 카테고리 Filter
    private BooleanExpression categoryFilter(String category) {
        if (StringUtils.isNullOrEmpty(category)) return QCategory.category1.category.eq("아우터");
        return QCategory.category1.category.eq(category);
    }

    // 재고 Filter
    private BooleanExpression isStock(String stock) {
        if (StringUtils.isNullOrEmpty(stock)) return null;
        if (stock.equals("1")) return null; // "1" : 품절상품 포함 -> null 리턴
        return QProduct.product.stock.ne(0L); // "1" : 품절상품 미포함 -> stock != 0 리턴
    }

    // 최소 금액 Filter
    private BooleanExpression minPriceRange(Long minPrice) {
        if (minPrice != null) return QProduct.product.price.goe(minPrice);
        return null;
    }

    // 최대 금액 Filter
    private BooleanExpression maxPriceRange(Long maxPrice) {
        if (maxPrice != null) return QProduct.product.price.loe(maxPrice);
        return null;
    }

    // 키워드 Filter
    // Full Text Search
    private BooleanExpression keywordMatch(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) return null;
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                "function('match',{0},{1})", QProduct.product.title, "+" + keyword + "*");
        return booleanTemplate.gt(0);
    }

    // 키워드 Filter
    private BooleanExpression keywordContain(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) return null;
        return QProduct.product.title.contains(keyword);
    }

    // 정렬
    private OrderSpecifier<?> sorting(String sorting) {

        if (StringUtils.isNullOrEmpty(sorting)) return qProduct.view.desc();

        switch (sorting) {
            case "조회순":
                return QProduct.product.view.desc();
            case "날짜순":
                return QProduct.product.createdTime.desc();
            case "10대":
                return QProductInfo.productInfo.ten.desc();
            case "20대":
                return QProductInfo.productInfo.twenty.desc();
            case "30대":
                return QProductInfo.productInfo.thirty.desc();
            case "40대 이상":
                return QProductInfo.productInfo.over_forty.desc();
        }
        return qProduct.view.desc();
    }


/*         --------------성능 테스트--------------         */

    /*@Override
    public Page<ProductMainResponseDto> mainFilter(Pageable pageable, String category, String stock,
                                                   Long minPrice, Long maxPrice, String keyword, String sorting) {



        // 데이터 수 줄여서 조회 테스트
        List<ProductMainResponseDto> result = queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct))
                .innerJoin(qProduct.category,qCategory)
                .innerJoin(qProduct.productInfo, qProductInfo)
                .where(categoryFilter(category),
                        isStock(stock),
                        minPriceRange(minPrice),
                        maxPriceRange(maxPrice),
                        keywordContain(keyword))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(sorting(sorting))
                .fetch();

        // full-text-search 적용
        List<ProductMainResponseDto> result = queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct))
                .innerJoin(qProduct.category,qCategory)
                .innerJoin(qProduct.productInfo, qProductInfo)
                .where(categoryFilter(category),
                        isStock(stock),
                        minPriceRange(minPrice),
                        maxPriceRange(maxPrice),
                        keywordMatch(keyword))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(sorting(sorting))
                .fetch();


        return new PageImpl<>(result, pageable, result.size());
    }*/

}
