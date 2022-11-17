package product.entity.product;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Stock {

    @Id
    private Long stock_id;
    @Column(nullable = false)
    private Long stock;

    public void order(Long orderNum){
        this.stock -= orderNum;
    }
    public void cancel(Long orderNum) { this.stock += orderNum;}
}
