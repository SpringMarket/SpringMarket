package product.entity.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private int view;

    @DateTimeFormat // 2022-11-08 15:07:26:1252156
    private LocalDateTime createdTime;

    @PrePersist
    public void createDate() {
        this.createdTime = LocalDateTime.now();
    }

    public void orderChangeStock(Long orderNum){
        this.stock -= orderNum;
    }
    public void cancelChangeStock(Long orderNum) { this.stock += orderNum;}


    @Column(nullable = false)
    private Long categoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_info_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductInfo productInfo;

}

