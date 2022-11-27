package product.service.product;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import product.service.RedisTestContainer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RedisTest extends RedisTestContainer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("Basic_Redis_Test")
    public void Basic_Redis_Test() {

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set("TE", "ST");

        assertEquals(values.get("TE"), "ST");
    }


    @BeforeAll
    public void Set_PipeLine() {

        ArrayList<String> list = new ArrayList<>();
        list.add("T");
        list.add("E");
        list.add("S");
        list.add("T!");

        RedisSerializer keySerializer = redisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            list.forEach(i -> {
                connection.stringCommands().set(keySerializer.serialize("Test::" + i),
                        valueSerializer.serialize(i));
            });
            return null;
        });
    }


    @Test
    @DisplayName("PipeLine_Test")
    public void PipeLine_Test() {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        assertEquals(values.get(("Test::T")), "T");
        assertEquals(values.get(("Test::E")), "E");
        assertEquals(values.get(("Test::S")), "S");
        assertEquals(values.get(("Test::T!")), "T!");
    }

    @BeforeAll
    public void Set_ZSet_PipeLine() {

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        RedisSerializer keySerializer = redisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            list.forEach(i -> {
                connection.zSetCommands().zAdd(keySerializer.serialize("ZSet"),
                        Math.random() * 10, valueSerializer.serialize(i));
            });
            return null;
        });
    }

    @Test
    @DisplayName("PipeLine_ZSet_Test")
    public void PipeLine_ZSet_Test() {
        ZSetOperations<String, String> values = redisTemplate.opsForZSet();
        assertEquals(values.size("ZSet"), 4);
    }
}