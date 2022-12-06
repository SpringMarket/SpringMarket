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



    public ProductMainResponseDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
    }
}
