/*
package product.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import product.MysqlTestContainer;
import product.entity.product.Product;
import product.entity.product.ProductInfo;
import product.entity.user.Authority;
import product.entity.user.User;
import product.repository.order.OrderRepository;
import product.repository.product.ProductInfoRepository;
import product.repository.product.ProductRepository;
import product.repository.user.UserRepository;
import product.service.order.OrderService;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ConcurrentOrderServiceTest{
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    public void beforeEach() {
//        userRepository.deleteAll();
//        orderRepository.deleteAll();
//        productRepository.deleteAll();
//        productInfoRepository.deleteAll();

        ProductInfo productInfo = ProductInfo.builder()
                .productInfoId(1L)
                .ten(10L)
                .twenty(10L)
                .thirty(10L)
                .over_forty(10L)
                .build();

        Product product_1 = Product.builder()
                .productId(1L)
                .title("Test_1")
                .content("Test_1")
                .photo("Test_1")
                .price(10000L)
                .stock(10L)
                .view(3)
                .createdTime(LocalDateTime.now())
                .categoryId(1L)
                .productInfo(productInfo)
                .build();

        productInfoRepository.save(productInfo);
        productRepository.save(product_1);
    }

    @AfterAll
    public void afterEach() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = "sy1@naver.com")
    public void concurrentOrder() throws InterruptedException {

        User user = User.builder()
                .email("sy1@naver.com")
                .password("password")
                .age("20대")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);

*/
/*        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<User> userList = userRepository.findAll();
        System.out.println(userList.get(0).getEmail());
        System.out.println(authentication.getName());*//*


        final int PRODUCT_STOCK = 10;
        final int THREAD_COUNT = 5; // PRODUCT_STOCK = 1000, THREAD_COUNT = 20000, FixedThreadPool = 32 ---> 13sec 715ms
        final int EXPECTED = PRODUCT_STOCK - THREAD_COUNT; // 10 - 5 = 5

        ExecutorService executorService = Executors.newFixedThreadPool(15);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        Product product = productRepository.findByTitle("Test_1");
        System.out.println(product.getProductId());

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    orderService.orderProcess(product.getProductId(), 1L,user);
                } */
/*catch (Exception e){
                    System.out.println(e.getMessage());
                    System.out.println(product.getStock());
                    System.out.println(orderRepository.findAll());
                }*//*

                finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Product product_2 = productRepository.findByProductId(product.getProductId());
        assertThat(product_2.getStock()).isEqualTo(EXPECTED);
//        assertThat(orderRepository.findAll().size()).isEqualTo(THREAD_COUNT);

    }

}
*/
