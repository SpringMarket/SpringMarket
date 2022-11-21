package product.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;

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
}
