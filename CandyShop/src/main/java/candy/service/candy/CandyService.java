package candy.service.candy;

import candy.dto.candy.CandyResponseDetailDto;
import candy.entity.candy.Candy;
import candy.entity.candy.Order;
import candy.entity.user.User;
import candy.exception.ExceptionType;
import candy.exception.RequestException;
import candy.repository.candy.CandyRepository;
import candy.repository.candy.OrderRepository;
import candy.service.RedisService;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class CandyService {
    private final AmazonS3Client amazonS3Client;
    private final CandyRepository candyRepository;
    private final OrderRepository orderRepository;
    private final RedisService redisService;


    @Value("${cloud.aws.s3.bucket}")
    String bucket;



    // Warm UP -> Named Post Put !
    @Transactional(readOnly = true)
    public void warmup() {

        log.info("Warm Up Start....");

        List<Candy> candy = candyRepository.findAll();
        List<CandyResponseDetailDto> candyResponseDetailDtos = new ArrayList<>();

        for (int r=0; r<500; r++) { // r=1
            if (r == candy.size()) break;
            candyResponseDetailDtos.add(CandyResponseDetailDto.toDto(candy.get(r)));
            redisService.setCandy("candy::" + candyResponseDetailDtos.get(r).getCandyId(), candyResponseDetailDtos.get(r), Duration.ofSeconds(100));
        }
    }



    // 사탕 전체 조회
    @Transactional(readOnly = true)
    public void findAllCandy(Pageable pageable) {

        log.info("Search All Log Start....");

        // Query DSL Filter 추가 ^___^

    }


    // 사탕 상세 조회 -> Cache Aside
    @Cacheable(value = "candy", key = "#id") // [post::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public void findCandy(Long id) {

        log.info("Search Once Log Start....");

        // 상세조회 로직 + 인덱싱

    }

    // 사탕 주문
    @Transactional(readOnly = true)
    public void orderCandy(Long id, Long orderNum, User user) {

        log.info("Order Start....");

        Candy candy = candyRepository.findById(id).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));
        candy.order(orderNum);

        Order order = new Order(candy, orderNum, user);

        orderRepository.save(order);
    }
}

