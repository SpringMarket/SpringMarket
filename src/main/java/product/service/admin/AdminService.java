package product.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.entity.product.Product;
import product.repository.product.ProductRepository;
import product.service.product.ProductRedisService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final ProductRedisService productRedisService;
    private final ProductRepository productRepository;


    // WarmUp -> Named Post PipeLine
    @Transactional
    public void warmupPipeLine(){

        log.info("Warm Up Named Post PipeLine Start....");

        for (long k =1; k<6; k++) {
            List<ProductDetailResponseDto> list = productRepository.warmupDetail(k);
            productRedisService.warmupPipeLine(list);
            list.clear();
        }
    }

    // WarmUp -> Ranking Board PipeLine
    @Transactional
    public void warmupRankingPipeLine(){

        log.info("Warm Up Ranking Board PipeLine Start....");

        for (long k =1; k<6; k++) {
            List<Product> list = productRepository.warmup(k);
            productRedisService.warmupRankingPipeLine(list, k);
            list.clear();
        }
    }

    // Warm UP -> Named Post << None PipeLine >>
    @Transactional
    public void warmup() {
        List<Product> warmupProduct = new ArrayList<>();

        for (long k =1; k<6; k++) {
            List<Product> list = productRepository.warmup(k);
            warmupProduct.addAll(list);
        }
        for (Product product : warmupProduct) {
            productRedisService.setProduct("product::" + product.getProductId(), ProductDetailResponseDto.toDto(product), Duration.ofDays(1));
        }
    }

    // Warm UP -> Ranking Board << None PipeLine >>
    @Transactional
    public void warmupRank() {
        for (long i=1; i<6; i++){
            List<Product> list = productRepository.warmup(i);
            for (int k = 0; k < 99; k++) {
                productRedisService.setRankingBoard("ranking::"+i, ProductMainResponseDto.toDto(list.get(k)), list.get(k).getView());
            }
        }
    }
}