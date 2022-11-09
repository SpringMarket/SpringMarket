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
    private String content;
    private String photo;
    private Long price;
    private Long stock;
    private Long view;
    private String createdTime;
    private Long categoryId;

    public ProductResponseDetailDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.content = product.getContent();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
        this.stock = product.getProductInfo().getStock();
        this.view = product.getProductInfo().getView();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime());
        this.categoryId = product.getCategory().getCategoryId();
    }


    /*public ProductResponseDetailDto(Long productId, String title, String content, String photo, Long price, Long stock, Long view, LocalDateTime createdTime) {
        this.productId = productId;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.price = price;
        this.stock = stock;
        this.view = view;
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(createdTime);
    }*/

    public static ProductResponseDetailDto toDto(Product product){
        return new ProductResponseDetailDto(
                product.getProductId(),
                product.getTitle(),
                product.getContent(),
                product.getPhoto(),
                product.getPrice(),
                product.getProductInfo().getStock(),
                product.getProductInfo().getView(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime()),
                product.getCategory().getCategoryId()
        );
    }
}
