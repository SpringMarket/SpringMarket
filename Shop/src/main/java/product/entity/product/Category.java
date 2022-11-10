package product.entity.product;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Category {

    @Id
    private Long categoryId;

    @Column(nullable = false)
    private String category;

}
