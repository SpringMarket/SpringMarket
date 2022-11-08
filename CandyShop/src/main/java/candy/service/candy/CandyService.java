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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class CandyService {
    private final CandyRepository candyRepository;
    private final OrderRepository orderRepository;
    private final RedisService redisService;


    @Value("${cloud.aws.s3.bucket}")
    String bucket;



    // Warm UP -> Named Post Put !
    @Transactional(readOnly = true)
    public void warmupCandy() {

        log.info("Warm Up Start....");

        List<Candy> candyCandy =candyRepository.warmup();

        for (Candy candy : candyCandy) {
            redisService.setCandy("candy::" + candy.getId(), CandyResponseDetailDto.toDto(candy), Duration.ofDays(1));
        }

        log.info("..... Success!");
    }



    // 사탕 전체 조회
    @Transactional(readOnly = true)
    public Page<CandyResponseDetailDto> findAllCandy(Pageable pageable, String category, Boolean stock, List<Long> price, String age, String keyword) {

        log.info("Search All Log Start....");

        return candyRepository.mainFilter(pageable, category,  stock, price, age, keyword);
    }


    // 사탕 상세 조회 -> Cache Aside
    @Cacheable(value = "candy", key = "#id") // [post::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public CandyResponseDetailDto findCandy(Long categoryId, Long id) {

        log.info("Search Once Log Start....");
        Candy candy = candyRepository.detail(categoryId, id);

        return CandyResponseDetailDto.toDto(candy);
    }

    // 사탕 주문
    @Transactional(readOnly = true) // -> start
    public void orderCandy(Long id, Long orderNum, User user) {

        log.info("Order Start....");

        Candy candy = candyRepository.findById(id).orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_EXCEPTION));
        candy.order(orderNum);

        Order order = new Order(candy, orderNum, user);

        orderRepository.save(order);
    }
}

