package product.dto.product;

import lombok.NoArgsConstructor;
import product.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
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

    public static ProductDetailResponseDto toDto(Product product){
        return new ProductDetailResponseDto(
                product.getProductId(),
                product.getTitle(),
                product.getContent(),
                product.getPhoto(),
                product.getPrice(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime())
        );
    }
}