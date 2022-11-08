package product.entity.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String age;

    @Enumerated(EnumType.STRING)
    private Authority authority;


    @Builder
    public User(String email, String password, String age, Authority authority) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.authority = authority;
    }
}
