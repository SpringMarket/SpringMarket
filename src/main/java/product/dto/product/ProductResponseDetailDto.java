package product.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotBlank
public class ProductResponseDetailDto {
    private Long productId;
    private String title;
    private String photo;
    private Long price;
    private Long stock;
    private int view;
    private String createdTime;
    private Long categoryId;

    public ProductResponseDetailDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
        this.stock = product.getStock().getStock();
        this.view = product.getView().getView();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime());
        this.categoryId = product.getCategory().getCategoryId();
    }

    public static ProductResponseDetailDto toDto(Product product){
        return new ProductResponseDetailDto(
                product.getProductId(),
                product.getTitle(),
                product.getPhoto(),
                product.getPrice(),
                product.getStock().getStock(),
                product.getView().getView(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime()),
                product.getCategory().getCategoryId()
        );
    }
}
