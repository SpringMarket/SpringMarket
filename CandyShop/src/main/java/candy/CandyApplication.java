package candy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CandyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandyApplication.class, args);
    }

}
