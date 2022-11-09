package product.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.NoArgsConstructor;
import product.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
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
    //private Long categoryId;

    /*@QueryProjection
    public ProductResponseDetailDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.content = product.getContent();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.view = product.getView();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime());
        this.categoryId = product.getCategory().getCategoryId();
    }*/

    public ProductResponseDetailDto(Long productId, String title, String content, String photo, Long price, Long stock, Long view, LocalDateTime createdTime) {
        this.productId = productId;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.price = price;
        this.stock = stock;
        this.view = view;
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(createdTime);
    }

    public static ProductResponseDetailDto toDto(Product product){
        return new ProductResponseDetailDto(
                product.getProductId(),
                product.getTitle(),
                product.getContent(),
                product.getPhoto(),
                product.getPrice(),
                product.getStock(),
                product.getView(),
                product.getCreatedTime()
                //product.getCategory().getCategoryId()

        );
    }
}
