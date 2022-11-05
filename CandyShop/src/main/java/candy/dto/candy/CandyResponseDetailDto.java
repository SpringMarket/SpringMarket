package candy.dto.candy;

import candy.entity.candy.Candy;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class CandyResponseDetailDto {
    private Long candyId;
    private String title;
    private String content;
    private String photo;
    private Long price;
    private Long stock;
    private Long view;
    private Long categoryId;

    public static CandyResponseDetailDto toDto(Candy candy){
        return new CandyResponseDetailDto(
                candy.getId(),
                candy.getTitle(),
                candy.getContent(),
                candy.getPhoto(),
                candy.getPrice(),
                candy.getStock(),
                candy.getView(),
                candy.getCategory().getId()

        );
    }
}
