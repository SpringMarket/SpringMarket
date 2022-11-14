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
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productInfoId;

    @Column(nullable = false)
    private Long ten;

    @Column(nullable = false)
    private Long twenty;

    @Column(nullable = false)
    private Long thirty;

    @Column(nullable = false)
    private Long over_forty;


    public void order(Long orderNum, String age){
        switch (age){
            case "10대":
                ten += orderNum; break;
            case "20대":
                twenty += orderNum; break;
            case "30대":
                thirty += orderNum; break;
            case "40대 이상":
                over_forty += orderNum; break;
        }
    }

    public void cancel(Long orderNum, String age){
        switch (age){
            case "10대":
                ten -= orderNum; break;
            case "20대":
                twenty -= orderNum; break;
            case "30대":
                thirty -= orderNum; break;
            case "40대 이상":
                over_forty -= orderNum; break;
        }
    }

}