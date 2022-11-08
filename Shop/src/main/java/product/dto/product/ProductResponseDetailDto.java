package product.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import product.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

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
    private String createdTime;
    private Long categoryId;

    @QueryProjection
    public ProductResponseDetailDto(Product product) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.content = product.getContent();
        this.photo = product.getPhoto();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.view = product.getView();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime());
        this.categoryId = product.getCategory().getId();
    }


    public static ProductResponseDetailDto toDto(Product product){
        return new ProductResponseDetailDto(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getPhoto(),
                product.getPrice(),
                product.getStock(),
                product.getView(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(product.getCreatedTime()),
                product.getCategory().getId()

        );
    }
}
