package product.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminRedisService {

    private final RedisTemplate<String, ProductDetailResponseDto> redisTemplateDetailDto;
    private final RedisTemplate<String, ProductMainResponseDto> redisTemplateMainDto;
    private final RedisTemplate<String, ProductRankResponseDto> redisTemplateRankDto;

    // Named Post PipeLine
    public void warmupPipeLine(List<ProductDetailResponseDto> list) {
        RedisSerializer keySerializer = redisTemplateDetailDto.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplateDetailDto.getValueSerializer();

        redisTemplateDetailDto.executePipelined((RedisCallback<Object>) connection -> {
            list.forEach(i -> {
                String key = "product::"+i.getProductId();
                connection.set(keySerializer.serialize(key), //stringCommands 수정
                        valueSerializer.serialize(i));
            });
            return null;
        });
    }

    // Ranking Board PipeLine
    public void warmupRankingPipeLine(List<ProductRankResponseDto> dtos, Long categoryId, int preference){
        RedisSerializer keySerializer = redisTemplateRankDto.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplateRankDto.getValueSerializer();

        redisTemplateRankDto.executePipelined((RedisCallback<Object>) connection -> {
            dtos.forEach(i -> {
                connection.zSetCommands().zAdd(keySerializer.serialize("Ranking::"+categoryId+"::"+preference),
                        i.getView(), valueSerializer.serialize(i));
            });
            return null;
        });
    }

    // 상품 상세 페이지 캐싱 -> NonePipeLine
    public void setProduct(String key, ProductDetailResponseDto data, Duration duration) {
        ValueOperations<String, ProductDetailResponseDto> values = redisTemplateDetailDto.opsForValue();
        values.set(key, data, duration);
    }

    // 랭킹보드 캐싱 -> NonePipeLine
    public void setRankingBoard(String key, ProductMainResponseDto data, double score) {
        ZSetOperations<String, ProductMainResponseDto> values = redisTemplateMainDto.opsForZSet();
        values.add(key, data, score);
    }
}
