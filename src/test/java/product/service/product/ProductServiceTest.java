package product.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {



    @Test
    @DisplayName("랭킹보드 조회 -> Default")
    void getRankingList() {
    }

    @Test
    @DisplayName("랭킹보드 조회 -> Not Exist Data")
    void getRankingListNotExistData() {
    }



    @Test
    @DisplayName("상품 필터링 조회 -> Default")
    void findAllProduct() {
    }

    @Test
    @DisplayName("상품 필터링 조회 -> Filter Null")
    void findAllProductFilterNull() {
    }



    @Test
    @DisplayName("상품 상세 조회 -> Default")
    void findProduct() {
    }

    @Test
    @DisplayName("상품 상세 조회 -> Cache Hit")
    void findProductCacheHit() {
    }

    @Test
    @DisplayName("상품 상세 조회 -> Not Exist Data")
    void findProductNotExistProduct() {
    }



    @Test
    @DisplayName("상품 조회수 추가 -> Exist Redis Key")
    void countViewExistKey() {}

    @Test
    @DisplayName("상품 조회수 추가 -> Not Exist Redis Key")
    void countViewNotExistKey() {}
}