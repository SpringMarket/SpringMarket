package product.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @Schema(description = "이메일",example = "kyeroromarket@gmail.com")
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식만 가능합니다.")
    private String email;

    @Schema(description = "회원 비밀번호",example = "kye1234")
    @NotBlank(message = "비밀번호를 입력해주세요.")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @Schema(description = "회원 연령대",allowableValues = {"10대", "20대", "30대","40대 이상"})
    @NotNull(message = "연령대를 선택해주세요.")
    @Pattern(regexp = "^10대|20대|30대|40대 이상$", message = "연령대를 다시 입력해주세요.")
    private String age;
}

