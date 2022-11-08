package product.dto.product;

import product.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NotBlank
public class ProductResponseDto {
    private Long productId;
    private String title;
    private String photo;
    private Long price;
    //private boolean stock;
    private Long stock;
    private Long view;
    private String createdTime;
    private Long categoryId;

    public static ProductResponseDto toDto(Product product){
        return new ProductResponseDto(
                product.getProductId(),
                product.getTitle(),
                product.getPhoto(),
                product.getPrice(),
                product.getStock(),
                product.getView(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime()),
                product.getCategory().getCategoryId()
        );
    }
}