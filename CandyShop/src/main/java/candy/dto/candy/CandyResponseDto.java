package candy.dto.candy;

import candy.entity.candy.Candy;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class CandyResponseDto {
    private Long id;
    private String title;
    private String photo;
    private Long price;
    //private boolean stock;
    private Long stock;
    private Long view;
    private Long categoryId;

    public static CandyResponseDto toDto(Candy candy){
        return new CandyResponseDto(
                candy.getId(),
                candy.getTitle(),
                candy.getPhoto(),
                candy.getPrice(),
                candy.getStock(),
                candy.getView(),
                candy.getCategory().getId()
        );
    }
}