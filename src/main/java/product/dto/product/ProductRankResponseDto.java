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

    public ProductRankResponseDto(Long productId, String title, String photo, Long price, int view, Long ten, Long twenty, Long thirty, Long over_forty) {
        this.productId = productId;
        this.title = title;
        this.photo = photo;
        this.price = price;
        this.view = view;
        this.ten = ten;
        this.twenty = twenty;
        this.thirty = thirty;
        this.over_forty = over_forty;
    }

    public static ProductRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple typedTuple){
        return (ProductRankResponseDto) typedTuple.getValue();
    }
}

