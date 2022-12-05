package product.dto.product;

import lombok.*;
import org.springframework.data.redis.core.ZSetOperations;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@NotBlank
public class ProductRankResponseDto {
    private Long productId;
    private String title;
    private String photo;
    private Long price;
    private int view;
    private Long ten;
    private Long twenty;
    private Long thirty;
    private Long over_forty;

    public ProductRankResponseDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
        this.view = product.getView();
        this.ten = product.getProductInfo().getTen();
        this.twenty = product.getProductInfo().getTwenty();
        this.thirty = product.getProductInfo().getThirty();
        this.over_forty = product.getProductInfo().getOver_forty();
    }

    public static ProductRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple typedTuple){
        return (ProductRankResponseDto) typedTuple.getValue();
    }
}

