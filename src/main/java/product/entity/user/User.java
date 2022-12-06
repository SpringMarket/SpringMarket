package product.entity.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String age;

    @Enumerated(EnumType.STRING)
    private Authority authority;


    public User() {
    }

    @Builder
    public User(String email, String password, String age, Authority authority) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.authority = authority;
    }
}
