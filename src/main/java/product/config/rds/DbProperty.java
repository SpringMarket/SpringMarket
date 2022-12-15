package product.config.rds;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.datasource")
public class DbProperty {

    private String url;
    private List<Replica> replicaList;

    private String driverClassName;
    private String username;
    private String password;

    @Getter @Setter
    public static class Replica {
        private String name;
        private String url;
    }
}