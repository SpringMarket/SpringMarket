package product.dto.product;

import product.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class ProductResponseDto {
    private Long id;
    private String title;
    private String photo;
    private Long price;
    //private boolean stock;
    private Long stock;
    private Long view;
    private Long categoryId;

    public static ProductResponseDto toDto(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getPhoto(),
                product.getPrice(),
                product.getStock(),
                product.getView(),
                product.getCategory().getId()
        );
    }
}