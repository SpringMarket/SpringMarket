# Spring Market
[![spring-market](https://github.com/SpringMarket/Market/actions/workflows/gradle.yml/badge.svg)](https://github.com/SpringMarket/Market/actions/workflows/gradle.yml)

![image](https://user-images.githubusercontent.com/112923814/206835670-2683c2ba-89d0-4509-bf81-4e5d2678ebca.png)

# 🌏프로젝트 소개
- **국내 최대 수량의 의류 데이터**를 보유한 쇼핑몰



⚙ [Spring Market 이용해보기 Click!](https://www.notion.so/1-3b015d8a07d149148b5fea36c4035ceb) <br>
⚙ [Spring Market 팀 노션 Click!](https://www.notion.so/1-3b015d8a07d149148b5fea36c4035ceb)

## ⚒ InfraStructure 

<img src ="https://user-images.githubusercontent.com/112923814/206205534-6b2cf6e1-9461-4258-bbc1-f54b762be4b8.jpg"/></a>      


##  Tech Stack
<img src ="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/></a>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"></a>
<img src ="https://img.shields.io/badge/Spring Securit-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/></a>
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<br>
<img src ="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/></a>
<img src ="https://img.shields.io/badge/Amazon AWS-232F3E?style=for-the-badge&logo=Amazon AWS&logoColor=white"/></a>
<img src ="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/></a> 

<details>
<summary>📣버전</summary>
<div markdown="1">    
  
### **Application**

- **JAVA 11**
- **Spring Boot** _2.7.0
- **Spring Security** _0.11.2
- **JPA**
- **Query DSL** _5.0.0
- **Full Text Search**

### **Data**

- **AWS RDS - MySQL** _8.028
- **AWS ElastiCache for Redis** _7.0.4
- **AWS S3**
- **Faker** (faker_15.2.0)

### **CI/CD**

- **Github Action**
- **AWS EC2**
- **AWS Elastic Beanstalk**

### **Monitoring**

- **Cloud Watch**
- **AWS OpenSearch**
- **Logback**

### TestCode

- **Junit 5**
- **Mock**
- **TestContainer**

### Front

- **React - yarn**
</div>
</details>


<details>
<summary>📣기술 스택 & 라이브러리 사용 이유</summary>
<div markdown="1">       
  

|기술 스택| 사용 이유|
|:--|:--|
|Query DSL|현준|
|Full Text Search| 현준 |
|RDS- MySQL| 현준 |
|AWS ElastiCache for Redis| 제윤 |
|Faker| 수영 |
|Github Action| 수영 |
|AWS Elastic Beanstalk| 제윤 |
|Cloud Watch| 제윤 |
|AWS OpenSearch| 제윤 |
|Logback| 수영 |
|Junit 5| 수영 |
|TestContainer|제윤|
|React|제윤|

</div>
</details>


## 🔥주요 기능


### 조회 기능
- 메인 페이지 로딩 속도는 플랫폼 첫 인상에 큰 영향을 주기 때문에 100ms 이내를 목표
- 쇼핑몰 홈페이지 로딩 속도가 2초 이상이라면 고객이 해당 페이지를 떠날 확률이 높기 때문에 고객의 서비스 체류 시간을 높이기 위해서
필터링 조회 속도를 2초 이내를 목표, 상세 조회는 500ms 이내를 목표

**1. Redis Sorted Set으로 메인 페이지 로딩 최적화 & 파이프라인 구축** <br>
Redis 파이프라인을 구축하여 메인페이지에 노출할 인기 상품 데이터를 Redis에 캐싱을 하고
Redis에 캐싱된 데이터를 사용해 메인 페이지 조회 성능 % 개선, 평균 로딩 속도 ~ms로 목표 달성

**2. Redis에 캐싱된 데이터로 상품 상세조회** <br>
카테고리별 인기 상품 ~개를 Redis에 캐싱하여 캐싱된 상품의 상세페이지 조회 성능 % 개선

**3. 결합 인덱스 추가, 커버링 인덱스 적용**<br>
조회수+pk로 결합 인덱스를 추가하여 조회순, 날짜순 정렬 시 성능 저하의 가장 큰 원인이었던 sort 부하를 해결하고
QueryDSL은 서브쿼리를 지원하지 않기 때문에 커버링 인덱스를 활용해 페이징 조회 성능을 1900% 개선 

**4. Full Text Search로 키워드 검색**<br>
키워드 조회 시 인덱스를 사용하지 않는 like 키워드 방식과 비교해 인덱스를 사용하여 조회하는 full-text-search 방식으로 변경하여 약 634% 성능 개선

**5. 데이터 반정규화**<br>
쿼리문에서 join문을 제거를 위해 데이터 반정규화를 하여 조회수, 재고수 테이블을 상품 테이블과 병합하여 
조회 성능을 66.6% 개선

## 🎯트러블 슈팅

<details>
<summary>📌 INDEX 중복</summary>
<div markdown="1">       

😎숨겨진 내용😎

</div>
</details>

<details>
<summary>📌 1만 건의 상품 데이터 Cache Warmup 동작 시 Latency 지연이 발생했습니다. </summary>
<div markdown="1">       

#### ❗ 문제상황
  - 상품 데이터의 빠른 조회와 DB 부하 분산을 위해 캐싱은 필수였습니다.
  - 하지만 TCP 기반으로 동작하는 Redis에 1만 건의 데이터를 개별로 Input 할 때 타임아웃 + 극심한 Latency 지연이 발생했습니다.
  ![Warmup NonePipeline Logic - Postman2 ](https://user-images.githubusercontent.com/112923814/206866704-34a1e734-5478-4d00-b12a-edfe693f02dd.png)
  
#### 💡 Solution : Redis Pipeline 구축
  - 작업의 단위를 직접 구축해서 요청이 가능해졌습니다. ( 다중 Insert 가능 )
  
#### ✔ 결과
  - 10,000건의 TCP 통신이 10건(+1000)으로 축소되었습니다.
  
![warmup rank ](https://user-images.githubusercontent.com/112923814/206866707-21c54446-dd68-4b61-ba97-92056cf27581.png)



</div>
</details>

<details>
<summary>📌 상품이 클릭될 때마다 조회수 +1 Update 요청이 발생했습니다.</summary>
<div markdown="1">       
  
  
#### 💡 Solution : Cache Write Back
  - 조회수를 캐시에 모아 일정 주기 배치 작업을 통해 DB에 반영
  - 싱글쓰레드인 Redis의 특성상 Atomic하게 Increment를 처리할 수 있다.
  - 조회 기능의 많은 I/O와 함께 발생하는 Update 쿼리를 컨트롤할 수 있다.  
  
#### ✔ 결과
  - 클릭 시마다 발생했던 Update 쿼리 -> 1시간 주기로 배치작업
  - 결과 자세하게 작성 필요..

</div>
</details>

<details>
<summary>📌 DB 분산과 서버 튜닝</summary>
<div markdown="1">       

😎숨겨진 내용😎

</div>
</details>

<details>
<summary>📌 동시성 제어의 동작 최적화</summary>
<div markdown="1">       

😎숨겨진 내용😎

</div>
</details>

<details>
<summary>📌 문제상황 작성</summary>
<div markdown="1">       

😎숨겨진 내용😎

</div>
</details>


## 🧑‍💻팀원

|송제윤|윤수영|계현준|
|:--|:--|:--|
|jy|sy|hj|

