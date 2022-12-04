package product.dto.order;

import product.entity.order.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NotBlank
public class MyPageResponseDto {
    private Long orderId;
    private String title;
    private Long price;
    private Long orderNum;
    private String orderStatus;
    private String orderTime;

    public MyPageResponseDto(Orders order){
        this.orderId = order.getOrderId();
        this.title = order.getProduct().getTitle();
        this.price = order.getProduct().getPrice();
        this.orderNum = order.getOrderNum();
        this.orderStatus = order.getOrderStatus();
        this.orderTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(order.getOrderTime());
    }

    public static MyPageResponseDto toDto(Orders order) {
        return new MyPageResponseDto(
                order.getOrderId(),
                order.getProduct().getTitle(),
                order.getProduct().getPrice(),
                order.getOrderNum(),
                order.changeStatus(order),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(order.getOrderTime())
        );
    }
}