package product.dto.product;

import lombok.NoArgsConstructor;
import product.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NotBlank
public class ProductResponseDto {
    private Long productId;
    private String title;
    private String content;
    private String photo;
    private Long price;
    private String createdTime;

    public static ProductResponseDto toDto(Product product){
        return new ProductResponseDto(
                product.getProductId(),
                product.getTitle(),
                product.getContent(),
                product.getPhoto(),
                product.getPrice(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime())
        );
    }
}