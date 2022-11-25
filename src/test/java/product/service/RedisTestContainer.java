package product.service;
// RedisTestContainer

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class RedisTestContainer {


    static final String REDIS_IMAGE = "redis:6-alpine";
    static final GenericContainer REDIS_CONTAINER;

        static {
            REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                    .withExposedPorts(6379)
                    .withReuse(true);

            REDIS_CONTAINER.start();
//            System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
//            System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
        }

        @DynamicPropertySource
        public static void overrideProps(DynamicPropertyRegistry registry){
            registry.add("SPRING_REDIS_HOST", REDIS_CONTAINER::getHost);
            registry.add("SPRING_REDIS_PORT", () -> ""+REDIS_CONTAINER.getFirstMappedPort());
        }
}