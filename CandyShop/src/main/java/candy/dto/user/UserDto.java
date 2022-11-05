package candy.dto.user;

import candy.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String email; // 로그인 아이디
    private String name; // 유저 실명

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

}
