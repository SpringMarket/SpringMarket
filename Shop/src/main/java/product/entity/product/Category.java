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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String category;

    public Category(String category) {
        this.category = category;
    }
}
