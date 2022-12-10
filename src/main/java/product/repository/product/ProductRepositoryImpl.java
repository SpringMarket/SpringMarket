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
import product.entity.product.QProduct;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QProduct qProduct = QProduct.product;

    // 필터링 조회
    @Override
    public Page<ProductMainResponseDto> mainFilter(Pageable pageable, Long categoryId, String stock,
                                                   Long minPrice, Long maxPrice, String keyword, String sorting) {

        if(sorting==null) sorting = "조회순";

        List<Long> ids = queryFactory.from(qProduct)
                .select(qProduct.productId)
                .where(categoryFilter(categoryId),
                        isStock(stock),
                        minPriceRange(minPrice),
                        maxPriceRange(maxPrice),
                        keywordContain(keyword))
                .orderBy(sorting(sorting))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // Null -> 공백 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<ProductMainResponseDto> result = queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct.productId,
                        qProduct.title,
                        qProduct.photo,
                        qProduct.price
                ))
                .where(qProduct.productId.in(ids))
                .orderBy(sorting(sorting))
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }

    // 키워드 조회
    @Override
    public Page<ProductMainResponseDto> keywordFilter(Pageable pageable, String keyword) {
        /*List<Long> ids = queryFactory.from(qProduct)
                .select(qProduct.productId)
                .where(keywordMatch(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Null -> 공백 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<ProductMainResponseDto> result = queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct))
                .where(qProduct.productId.in(ids))
                .fetch();*/

        List<ProductMainResponseDto> result = queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct.productId,
                        qProduct.title,
                        qProduct.photo,
                        qProduct.price
                        ))
                .where(keywordMatch(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        System.out.println(keyword);

        return new PageImpl<>(result, pageable, result.size());
    }

    // 상세 조회 -> return ProductDetailResponseDto
    @Override
    public ProductDetailResponseDto detail(Long productId) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductDetailResponseDto.class,
                        qProduct.productId,
                        qProduct.title,
                        qProduct.content,
                        qProduct.photo,
                        qProduct.price,
                        qProduct.createdTime
                ))
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }

    // 카트 조회 -> return ProductMainResponseDto
    @Override
    public List<ProductMainResponseDto> cartList(List<Long> ids, Pageable pageable) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductMainResponseDto.class,
                        qProduct.productId,
                        qProduct.title,
                        qProduct.photo,
                        qProduct.price
                ))
                .where(qProduct.productId.in(ids))
                .orderBy(sorting("조회순"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // 조회수 Update Query
    @Override
    public void addView(Long productId, int viewCnt) {
        queryFactory
                .update(qProduct)
                .where(qProduct.productId.eq(productId))
                .set(qProduct.view, viewCnt)
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


    /*  FILTER (｡•̀ᴗ-)✧ */

    // 조회순
    private BooleanExpression viewIndex(String sorting) {
        if (StringUtils.isNullOrEmpty(sorting)) return qProduct.view.loe(0);
        if (sorting.equals("조회순")) return qProduct.view.loe(0);
        return null;
    }

    // 카테고리 Filter
    private BooleanExpression categoryFilter(Long categoryId) {
        if (categoryId != null) return qProduct.categoryId.eq(categoryId);
        return null;
    }

    // 재고 Filter
    private BooleanExpression isStock(String stock) {
        if (StringUtils.isNullOrEmpty(stock)) return null;
        if (stock.equals("1")) return null; // "1" : 품절상품 포함 -> null 리턴
        return qProduct.stock.ne(0L); // "1" : 품절상품 미포함 -> stock != 0 리턴
    }

    // 최소 금액 Filter
    private BooleanExpression minPriceRange(Long minPrice) {
        if (minPrice != null) return qProduct.price.goe(minPrice);
        return null;
    }

    // 최대 금액 Filter
    private BooleanExpression maxPriceRange(Long maxPrice) {
        if (maxPrice != null) return qProduct.price.loe(maxPrice);
        return null;
    }

    // 키워드 Filter
    // Full Text Search
    private BooleanExpression keywordMatch(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) return null;
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                "function('match',{0},{1})", qProduct.title, "+" + keyword + "*");
        return booleanTemplate.gt(0);
    }

    // 키워드 Filter
    private BooleanExpression keywordContain(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) return null;
        return qProduct.title.contains(keyword);
    }

    // 정렬
    private OrderSpecifier<?> sorting(String sorting) {
        if (StringUtils.isNullOrEmpty(sorting)) return qProduct.view.asc();
        if (!sorting.equals("조회순")) return qProduct.productId.desc();
        return qProduct.view.asc();
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
    */

}
