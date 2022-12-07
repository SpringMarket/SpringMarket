package product.dto.product;

import lombok.Getter;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;

@Getter
@NotBlank
public class ProductMainResponseDto {
    private Long productId;
    private String title;
    private String photo;
    private Long price;

    public ProductMainResponseDto(Long productId, String title, String photo, Long price) {
        this.productId = productId;
        this.title = title;
        this.photo = photo;
        this.price = price;
    }
}
