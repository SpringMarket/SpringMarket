package product;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;


public class MysqlTestContainer {

    static final String MY_SQL_IMAGE = "mysql:8.0.28";
    static MySQLContainer<?> MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>(MY_SQL_IMAGE)
                .withExposedPorts(3306)
                .withReuse(true);

        MY_SQL_CONTAINER.start();
    }


//    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>(MY_SQL_IMAGE)
//            .withInitScript("schema.sql");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }
}