package product.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;
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

    private final AdminRedisService adminRedisService;
    private final ProductRepository productRepository;


    // WarmUp -> Named Post PipeLine
    @Transactional
    public void warmupPipeLine(Long categoryId){

        log.info("Warm Up Named Post PipeLine Start....");

        List<ProductDetailResponseDto> list = productRepository.warmupNamedPost(categoryId);
        adminRedisService.warmupPipeLine(list);
    }

    // WarmUp -> Ranking Board PipeLine
    @Transactional
    public void warmupRankingPipeLine(Long categoryId){

        log.info("Warm Up Ranking Board PipeLine Start....");

        List<ProductRankResponseDto> list = productRepository.warmupRankingBoard(categoryId);
        adminRedisService.warmupRankingPipeLine(list, categoryId);
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
            adminRedisService.setProduct("product::" + product.getProductId(), ProductDetailResponseDto.toDto(product), Duration.ofDays(1));
        }
    }

    // Warm UP -> Ranking Board << None PipeLine >>
    @Transactional
    public void warmupRank() {
        for (long i=1; i<6; i++){
            List<Product> list = productRepository.warmup(i);
            for (int k = 0; k < 99; k++) {
                adminRedisService.setRankingBoard("ranking::"+i, ProductMainResponseDto.toDto(list.get(k)), list.get(k).getView());
            }
        }
    }
}
