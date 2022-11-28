package product.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotBlank
public class ProductRankResponseDto {
    private Long productId;
    private String title;
    private String photo;
    private Long price;
    private int view;

    public ProductRankResponseDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
        this.view = product.getView();
    }

    public static ProductRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple typedTuple){
        return (ProductRankResponseDto) typedTuple.getValue();
    }
}

