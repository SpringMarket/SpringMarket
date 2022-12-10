package product.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.dto.product.ProductDetailResponseDto;
import product.dto.product.ProductMainResponseDto;
import product.dto.product.ProductRankResponseDto;
import product.entity.product.Product;
import product.repository.admin.AdminQueryRepository;
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
    private final AdminQueryRepository adminQueryRepository;


    // WarmUp -> Named Post PipeLine
    @Transactional
    public void warmupPipeLine(Long categoryId){

        log.info("Warm Up Named Post PipeLine Start....");

        List<ProductDetailResponseDto> list = adminQueryRepository.warmupNamedPost(categoryId);

        adminRedisService.warmupPipeLine(list);
    }

    // WarmUp -> Ranking Board PipeLine
    @Transactional
    public void warmupRankingPipeLine(Long categoryId){

        log.info("Warm Up Ranking Board PipeLine Start....");
        List<ProductRankResponseDto> list = adminQueryRepository.warmupRankingBoard(categoryId);
        for (int i=1; i<5; i++) adminRedisService.warmupRankingPipeLine(list, categoryId, i);
    }


    // Warm UP -> Named Post << None PipeLine >>
    @Transactional
    public void warmup(Long categoryId) {
        List<ProductDetailResponseDto> list = adminQueryRepository.warmupNamedPost(categoryId);
        for (ProductDetailResponseDto product : list) {
            adminRedisService.setProduct("product::" + product.getProductId(), product, Duration.ofDays(1));
        }
    }
}
