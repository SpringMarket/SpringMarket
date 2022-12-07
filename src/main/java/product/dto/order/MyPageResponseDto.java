package product.dto.order;

import lombok.*;
import product.entity.order.Orders;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NotBlank
@NoArgsConstructor
@Getter
public class MyPageResponseDto {
    private Long orderId;
    private Long productId;
    private String title;
    private Long price;
    private Long orderNum;
    private String orderStatus;
    private String orderTime;

    public MyPageResponseDto(Long orderId, Long productId, String title, Long price, Long orderNum, String orderStatus, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.orderNum = orderNum;
        this.orderStatus = orderStatus;
        this.orderTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(orderTime);
    }
}