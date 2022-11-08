package product.dto.mypage;

import product.entity.product.Order;
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

    public static MyPageResponseDto toDto(Order order) {
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