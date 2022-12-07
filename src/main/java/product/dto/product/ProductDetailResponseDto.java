package product.dto.product;

import lombok.*;
import product.entity.product.Product;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
//@EqualsAndHashCode
@NoArgsConstructor
@NotBlank
public class ProductDetailResponseDto {
    private Long productId;
    private String title;
    private String content;
    private String photo;
    private Long price;
    private String createdTime;

    public ProductDetailResponseDto(Long productId, String title, String content, String photo, Long price, LocalDateTime createdTime) {
        this.productId = productId;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.price = price;
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(createdTime);
    }
}