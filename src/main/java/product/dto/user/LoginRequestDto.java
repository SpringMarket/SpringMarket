package product.dto.user;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDto {

    @Schema(description = "이메일",example = "kyeroromarket@gmail.com")
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식만 가능합니다.")
    private String email;


    @Schema(description = "회원 비밀번호",example = "kye1234")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;



    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
