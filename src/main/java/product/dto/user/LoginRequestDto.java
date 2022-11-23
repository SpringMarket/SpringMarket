package product.dto.user;

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

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식만 가능합니다.")
    private String email;


    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;



    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
