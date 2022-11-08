package product.entity.product;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
@Entity
public class Category {

    @Id
    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String category;
}
