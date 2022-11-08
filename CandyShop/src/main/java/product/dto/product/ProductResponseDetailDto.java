package product.dto.product;

import product.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class ProductResponseDetailDto {
    private Long productId;
    private String title;
    private String content;
    private String photo;
    private Long price;
    private Long stock;
    private Long view;
    private Long categoryId;

    public static ProductResponseDetailDto toDto(Product product){
        return new ProductResponseDetailDto(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getPhoto(),
                product.getPrice(),
                product.getStock(),
                product.getView(),
                product.getCategory().getId()

        );
    }
}
