package product.entity.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productInfoId;

    @Column(nullable = false)
    private Long ten;

    @Column(nullable = false)
    private Long twenty;

    @Column(nullable = false)
    private Long thirty;

    @Column(nullable = false)
    private Long forty;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private Long view;


    public void order(Long orderNum){
        this.stock -= orderNum;
    }
    public void cancel(Long orderNum) { this.stock += orderNum;}


}