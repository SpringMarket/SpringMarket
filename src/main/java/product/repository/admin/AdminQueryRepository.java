package product.repository.admin;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.Product;
import product.entity.product.QProduct;
import product.entity.product.QProductInfo;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AdminQueryRepository {

    private final JPAQueryFactory queryFactory;

    QProduct qProduct = QProduct.product;
    QProductInfo qProductInfo = QProductInfo.productInfo;

    // WarmUp -> Return ProductDetailResponseDto Category Top 100
    public List<ProductDetailResponseDto> warmupNamedPost(Long categoryId) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductDetailResponseDto.class, qProduct))
                .where(qProduct.categoryId.eq(categoryId))
                .orderBy(qProduct.view.desc())
                .limit(100)
                .fetch();
    }

    // WarmUp -> Return ProductDetailResponseDto Category Top 100 -> 나이별 선호도 정렬 추가
    public List<ProductRankResponseDto> warmupRankingBoard(Long categoryId) {
        return queryFactory.from(qProduct)
                .select(Projections.constructor(ProductRankResponseDto.class, qProduct))
                .where(qProduct.categoryId.eq(categoryId))
                .orderBy(qProduct.view.asc())
                .limit(100)
                .fetch();
    }

    // WarmUp -> Return Product Category Top 100
    public List<Product> warmup(Long categoryId) {
        return queryFactory.from(qProduct)
                .select(qProduct)
                .where(qProduct.categoryId.eq(categoryId))
                .orderBy(qProduct.view.desc())
                .limit(100)
                .fetch();
    }
}
