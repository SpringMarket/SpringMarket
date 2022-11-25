package product.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotBlank
public class ProductMainResponseDto {
    private Long productId;
    private String title;
    private String photo;
    private Long price;

    public ProductMainResponseDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
    }

    public static ProductMainResponseDto toDto(Product product){
        return new ProductMainResponseDto(
                product.getProductId(),
                product.getTitle(),
                product.getPhoto(),
                product.getPrice()
        );
    }

    public static ProductMainResponseDto convertToResponseRankingDto(TypedTuple typedTuple){
        return (ProductMainResponseDto) typedTuple.getValue();
    }
}
