package product.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotBlank
public class ProductCreateDto {
    private String title;
    private String content;
    private String photo;
    private Long price;
}
