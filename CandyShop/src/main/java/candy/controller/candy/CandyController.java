package candy.controller.candy;

import candy.entity.user.User;
import candy.exception.ExceptionType;
import candy.exception.RequestException;
import candy.repository.user.UserRepository;
import candy.response.Response;
import candy.service.candy.CandyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static candy.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CandyController {
    private final CandyService candyService;
    private final UserRepository userRepository;


    // Warm UP
    @GetMapping("/candycandy")
    public Response warmupCandy() {

        candyService.warmupCandy();
        return success();
    }

    // 메인 페이지
    // orderBy first(JPA) or last(Query DSL) 성능테스트
    @GetMapping("/candy")
    public Response findAllCandy(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "stock", required = false) Boolean stock,
                                 @RequestParam(value = "price", required = false) List<Long> price,
                                 @RequestParam(value = "age", required = false) String age,
                                 @RequestParam(value = "keyword", required = false) String keyword
                                 ) {

        candyService.findAllCandy(pageable, category, stock, price, age, keyword);
        return success();
    }

    // 상세 페이지
    @GetMapping("/candy/{id}")
    public Response findCandy(@PathVariable Long id) {

        candyService.findCandy(id);
        return success();
    }

    // 상품 주문
    @PostMapping("/candy/{id}/order")
    public Response order(@PathVariable Long id, @RequestBody Long orderNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RequestException(ExceptionType.ACCESS_DENIED_EXCEPTION));
        // () -> redirect login.html
        candyService.orderCandy(id, orderNum, user);
        return success();
    }
}
