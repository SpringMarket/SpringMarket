package product.entity.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long view_id;

    @Column(nullable = false)
    private Long view;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}
