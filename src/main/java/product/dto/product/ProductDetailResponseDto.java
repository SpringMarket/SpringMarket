package product.dto.product;

import lombok.*;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@NotBlank
public class ProductDetailResponseDto {
    private Long productId;
    private String title;
    private String content;
    private String photo;
    private Long price;
    private String createdTime;

    public ProductDetailResponseDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.content = product.getContent();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime());
    }
}