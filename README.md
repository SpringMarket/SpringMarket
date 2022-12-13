# 🌿Spring Market

![image](https://user-images.githubusercontent.com/112923814/207095093-e2d922cd-5dc5-4036-859c-c10890e416b7.png)
<br/>


# 🌏 프로젝트 소개
- **국내 최대 수량의 의류 데이터 (10,000,000)** 를 보유한 쇼핑몰입니다.



✔ [Spring Market 이용해보기 Click!](https://spring-market.vercel.app/) <br>
✔ [Spring Market 팀 노션 Click!](https://www.notion.so/1-3b015d8a07d149148b5fea36c4035ceb)
<br/><br/>
# ⚙ InfraStructure 

![InfraStructure2](https://user-images.githubusercontent.com/112923814/207117134-890a5ce5-fc20-4b9b-a055-87cf071dd654.jpg)


# ⛓ ERD
<details>
<summary><strong> OPEN </strong></summary>
<div markdown="1">       
</br>

![image](https://user-images.githubusercontent.com/112923814/207008488-d2395e48-ea03-4744-aa31-492b1d4fecf6.png)

</div>
</details>
</br>

# 🧬API
✔ [Spring Market API Click!](https://documenter.getpostman.com/view/23647730/2s8YzP3RB4)
</br></br></br>


# 🕹 Tech Stack
### ✔ Back-end </br>
<img src ="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/></a>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"></a>
<img src ="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/></a>
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"></br>
<img src="https://img.shields.io/badge/JPA-999933?style=for-the-badge&logo=JPA&logoColor=white"></a>
<img src ="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<img src ="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/></a>
### ✔ DevOps
<img src ="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"/></a>
<img src ="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"/></a>
<img src ="https://img.shields.io/badge/AWS Elastic Beanstalk-336633?style=for-the-badge&logo=AWS Elastic Beanstalk&logoColor=white"/></a></br>
<img src ="https://img.shields.io/badge/Amazon CloudWatch-FF4F8B?style=for-the-badge&logo=Amazon CloudWatch&logoColor=white"/></a>
<img src ="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"/></a>
### ✔ Front-end
<img src ="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white"/></a>
<img src="https://img.shields.io/badge/React Router-CA4245?style=for-the-badge&logo=React Router&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=black"/></a>
<img src="https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=Vercel&logoColor=white"></a>
</br>
<details>
<summary><strong>📣버전</strong></summary>
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

### **TestCode**

- **Junit 5**
- **Mock**
- **TestContainer**

### **Front**

- **React - yarn**
</div>
</details>



<details>
<summary><strong>📣기술 & 라이브러리 사용 이유</strong></summary>
<div markdown="1">   
  <br/>
  
<!-- 
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
|React|제윤| -->

 
  
  <details>
  <summary><strong> 1️⃣ Query DSL</strong></summary>
    <div markdown="1"> 
      
1. 동적인 쿼리 작성이 편리하다.
      -  프로젝트 특성 상 조회 시 여러 가지 조건에 따라 동적으로 쿼리문이 실행되어야 하는 상황이 존재
2. 자동 완성 등 IDE의 도움을 받을 수 있다.
3. 문자가 아닌 코드로 쿼리를 작성함으로써, 컴파일 시점에 문법 오류를 쉽게 확인할 수 있다.
      - 쿼리문을 작성하며 자동완성과 문법 오류를 검증할 수 있기 때문에 편리함
4. 쿼리 작성 시 제약 조건 등을 메서드 추출을 통해 재사용할 수 있다.
      - 쿼리문이 길어지게 되면 가독성이 떨어지는데 메서드를 사용하여 재사용하여 쿼리문의 길이를 줄이면 가독성이 증가함
      
  </details> 
  
  <details>
  <summary><strong> 2️⃣ Full Text Search</strong></summary>
    <div markdown="1">     
      
  - 대용량 데이터를 조회하기 위해서는 like 키워드는 너무 성능이 떨어진다고 생각되어 인덱스를 사용하여 조회하는 full text index를 사용하면 조회 성능이 향상될 것으로 예상되어 사용하게 됬다.
  - 1000만건이 되는 데이터의 키워드를 조회하려고 하기 like 키워드만으로 조회를 하기에는 한계가 있다는 것을 느꼈다. 1000만 건의 5분의 1 수준 200만건을 조회하는데도 32.09초가 걸려 목표로하는 2초 이내의 조회를 하기에는 많이 부족해 보여서 인덱스를 활용하여 조회하는 full-text-search를 도입하여 조회 성능을 향상시키고자 했다.
      
    </div>
  </details> 
  
  <details>
  <summary><strong> 3️⃣ RDS- MySQL</strong></summary>
    <div markdown="1">     
    <br>
대용량 데이터를 다루기 때문에 PostgreSQL과 MySQL 사이에서 고민을 했는데 MySQL을 선택한 이유에 대해서 작성해 보았다.

1. 프로젝트 전 MySQL을 미리 학습한 경험이 있어 빠르게 마무리해야 하는 프로젝트 특성 상 다른 DB보다 빠르게 프로젝트에 적용이 가능하기 때문에 선택하게 됬다.
2. 단순 CRUD 시 MySQL의 성능이 조금 더 우수하다. 
    
    PostgreSQL은 Update시 MySQL과는 다르게 변경 전 값을 삭제 마크처리 후 변경 후 값을 새 행으로 추가하는 방식으로 작업이 진행되기 때문에 Update의 성능이 떨어진다. 하지만 Update 기능이 많이 일어나는 프로젝트 특성 상 MySQL을 사용하는 것이 적합하다고 생각됐다. (그래서 PostgreSQL은 보통 Insert, Select 위주의 서비스에 사용된다)
    

3. 현업에서 MySQL의 점유율 높기 때문에 레퍼런스를 찾기 쉽다.
    
    PostgreSQL도 4위의 점유율과 급격하게 성장하고 있지만 MySQL은 전 세계 2위의 점유율을 가지고 있고 오랫동안 높은 점유율을 가지고 있기 때문에 개발을 처음 공부하는 입장으로서 많은 레퍼런스들을 참고 할 수 있다는 점에서 선택하였다.
    
4. 전문 검색 기능을 사용할 수 있다.
    
    MySQL 과 PostgreSQL 모두 ‘full-text-search’ 전문 검색 기능을 지원하고 있다.
    </div>
  </details> 
  
    
  <details>
  <summary><strong> 4️⃣ AWS ElastiCache for Redis</strong></summary>
    <div markdown="1">
      AWS 아키텍처로 구성된 프로젝트에서 최적화된 서비스와 
팀 프로젝트 환경에서 효과적인 모니터링을 위해서 사용
    
  </details> 
  
    
  <details>
  <summary><strong> 5️⃣ Faker & SQLAlchemy</strong></summary>
    <div markdown="1">     
      
- 1000만건 상품 데이터를 목표로 하였기 때문에 실제 의류 쇼핑몰 크롤링을 하기에는 무리가 있다고 판단하여 더미데이터 생성을 하기로 결정하였습니다.
- 카테고리별로 다른 상품 이름, 사진을 생성해야 하고, 상품과 주문 생성일자가 Primary key인 id에 따라 증가하도록 데이터를 구성할 수 있어야 하고 생성한 대량의 데이터를 손쉽게 RDS MySQL DB로 보낼 수 있어야 했습니다.
- 파이썬 Faker 라이브러리로 위 조건을 만족하는 더미데이터를 생성했고 Python과 MySQL을 연결시켜주는 라이브러리 mysqlclient를 설치하고 파이썬의 ORM인 SQLAlchemy를 이용하여 생성한 더미데이터 1000만 건을 데이터베이스에 입력했습니다.
    
  </details> 
  
    
    
  <details>
  <summary><strong> 6️⃣ Github Action</strong></summary>
    <div markdown="1">     
    
    - 비용에 문제가 발생하지 않습니다.
    - 클라우드에서 동작하므로 서버 설치가 필요하지 않습니다.
    - Github Repository로 관리하는 프로젝트이기에 호환이 좋고 Github 이벤트(ex. PR) 처리가 가능합니다.
    - GitHub의 완전 관리 서비스이기에 사용이 편리하기에 인프라를 관리하는데 드는 코스트를 낮출 수 있습니다.
      
    </div>
  </details> 
  
    
    
  <details>
  <summary><strong> 7️⃣ AWS Elastic Beanstalk</strong></summary>
    <div markdown="1">
<!--  -->
- 다양한 인프라 서비스를 간편하게 사용할 수 있습니다.
< 용량 프로비저닝, 로드 밸런싱, 모니터링, 협업 도구 >
- Github에서 통합이 가능
- 완전 관리형 서비스 사용으로 프로젝트에서 인프라에 사용하는 코스트를 낮출 수 있음
  
    </div>
  </details> 
  
    
    
  <details>
  <summary><strong> 8️⃣ Logback</strong></summary>  
  <div markdown="1">     
  
  - 개발 과정에서 문제 원인 파악 및 개발의 안정성 확보를 위해 콘솔 로그 외의 로그 관리의 필요성을 느꼈습니다.
  - Spring Boot에서 로깅은 대표적으로 Log4j, Logback, Log4j2으로 로그 구현체를 사용합니다.
  - Log4j는 가장 오래된 프레임워크이며 2015년에 단종되었기 때문에 선택지에서 제외하였습니다.
  - Logback은 Log4j를 개발한 개발자가 개발한 Log4j의 후속 버전으로 지속적으로 업데이트되고 있습니다.
  - Log4j2는 가장 최근에 나온 로깅 프레임워크로 logback과 마찬가지로 필터링 기능과 자동 리로드 기능을 가지고 있습니다. logback과의 가장 큰 차이점은 Multi Thread 환경에서 비동기 로거(Async Logger)의 경우 log4j, logback 보다 처리량이 더 높고, 대기 시간이 훨씬 짧습니다. 또한 람다 표현식과 사용자 정의 로그 레벨도 지원합니다.
  - 로깅으로 DB관련 로그, 에러 로그, api 통신 로그를 분리해 파일로 관리하는 것을 목표로 했기 때문에 스프링 기본 설정인 logback 사용을 결정하였습니다.     
  - ConsoleAppender로 Info레벨 이상 로그를 콘솔에 출력,RollingFileAppender로 Debug레벨 이상 DB관련 로그와 Warn레벨 이상 에러로그를 각각 다른 파일로 저장하고
logback-access 모듈을 이용해 api 통신 관련 통신 로그 또한 파일로 저장하여 모니터링하였습니다.
 </div>
  </details> 
  
    
    
  <details>
  <summary><strong> 9️⃣ JUnit5 & Jacoco</strong></summary>
    <div markdown="1">     
      
 - Spring Boot 2.2.0 버전부터 JUnit5가 기본으로 채택되었으며 JUnit4보다 다양한 기능이 제공되어 JUnit5로 테스트 코드를 작성하였습니다.<br/>
 - 컨트롤러 테스트는 @WebMvcTest 어노테이션으로 Web Layer에만 집중하여 테스트하였으며, 서비스 테스트는 모듈 간의 상호작용이 정상적으로 수행되는지 확인하기 위해 통합 테스트를 진행했습니다.<br/>
 - JUnit 테스트 결과를 바탕으로 커버리지를 결과를 리포트해주는 Jacoco 라이브러리를 도입하여 구문 커버리지를 측정하며 안 쓰이는 코드와 어노테이션을 확인하며 리팩토링을 진행하였습니다.
 </div>
  </details> 
  
      
    
  <details>
  <summary><strong> 🔟 TestContainer</strong></summary>
    <div markdown="1">     
    <br>
Redis를 사용한 코드를 어느 환경에서든 바로 테스트가 가능하게 하기 위해서 사용 
<!--     </div> -->
  </details> 
  
  
</div>
</details>
<br/>


## 🔥 주요 기능


### ✔ 검색

- <details><summary><strong> 📢 Latency 목표값 설정 기준 Click!</strong></summary><div markdown="1"></br><pre><strong>KISSmetrics는 고객의 47%가 2초 이내의 시간에 로딩이 되는 웹 페이지를 원하고 있으며, 40%는 로딩에 3초 이상 걸리는 페이지를 바로 떠난다고 설명했습니다.</strong></pre></br></div></details>
- 메인 페이지의 로딩 속도는 플랫폼 첫 인상에 큰 영향을 주기에 100ms 이내를 목표했습니다.
- 필터링/키워드 조회 속도 2초, 상세 조회는 400ms 이내를 목표했습니다.
</br> 
<details> 
<summary><strong> 1⃣ Redis Sorted Set - 랭킹보드 구현을 통한 메인 페이지 로딩 최적화 </strong></summary>
<div markdown="1">      
</br> 
 
  - **Redis Sorted Set을 통해 평균 100ms의 속도로 랭킹보드를 제공하고 있습니다.**
  - 메인 페이지에 접근할 때마다 Order By가 동작하는 기존의 코드보다 **28배 성능이 향상**되었습니다. ( 28s -> 100ms )
  - 파이프라인 + 스케줄러를 통해 주기적으로 랭킹보드를 세팅하고 있습니다.</br></br>
  
</div>
</details>

<details>
<summary><strong> 2⃣ Redis Cache Aside - 상품 상세페이지 캐싱</strong></summary>
<div markdown="1"> 
<br>

  - **카테고리별 상위 5,000개의 상품 페이지를 캐싱하여 사용하고 있습니다.**
  - Redis 캐시 데이터를 통해 **DB의 부하를 최소화**했습니다.
  - 파이프라인을 통해 Cache Warmup을 동작시키고 있습니다.</br></br>
  
</div>
</details>

<details>
<summary><strong> 3⃣ 결합 인덱스 추가, 커버링 인덱스 적용</strong></summary>
<div markdown="1">       
<br>

  - **조회수+pk로 결합 인덱스를 추가하여 조회순, 날짜순 정렬 시 성능 저하의 가장 큰 원인이었던 sort 부하를 해결했습니다.**
  - QueryDSL은 서브쿼리를 지원하지 않기 때문에 커버링 인덱스를 활용해 **페이징 조회 성능을 1900% 개선**했습니다. </br></br>
  
</div>
</details>

<details>
<summary><strong> 4⃣ Full Text Search로 키워드 검색</strong></summary>
<div markdown="1">    
<br>

  - 키워드 조회 시 Full-Text-Search 방식을 사용하여 like문을 사용한 쿼리보다 **약 634% 성능을 개선**했습니다.</br></br>
  
</div>
</details>

<details>
<summary><strong> 5⃣ 데이터 반정규화</strong></summary>
<div markdown="1">       
<br>

  - 쿼리문에서 join문을 제거를 위해 데이터 반정규화를 하여 조회수, 재고수 테이블을 상품 테이블과 병합했고</br> **조회 성능을 66.6% 개선**했습니다.</br>
  
</div>
</details>
<br/>

### ✔ 주문
  - <details><summary><strong> 📢 동시성 제어 목표값 설정 기준 Click!</strong></summary><div markdown="1"></br><pre><strong>온라인 패션 스토어 무신사가 선보인 패션 특화 라이브 방송 ‘무신사 라이브’ 메종 키츠네 편이 방송 시작 5분 만에 매출 1억 원을 돌파했습니다.</br>동시 상품의 주문으로 가정했을 때 300초 동안 2000건의 주문이 발생한 상황입니다.</strong> (상품 가격 50,000원 기준)</pre></div></details>
  - 대규모 트래픽 상황에서 주문과 재고 데이터의 정합성은 서비스의 신뢰도에 큰 영향을 주고 있습니다.
  - 안정적인 동시성 제어를 위해 목표치를 '무신사 라이브 메종 키츠네 편'의 15배로 설정했습니다.
  - **( 30초 동안 동시 주문 3000건에 대한 정합성 유지 )**

<details>
<summary><strong> 1⃣ Pessimistic Lock으로 동시성 제어</strong></summary>
<div markdown="1">   
<br>

  - 트랜잭션이 시작될 때 MySQL DB에 Exclusive Lock을 걸어 Race Condition을 해결했습니다.
  - 서비스 코드에서 재고 변경 로직의 트랜잭션을 분리해 효율적으로 처리했습니다.
  - **부하테스트 결과 30초 동안 이루어지는 3000건의 동일 상품 주문 데이터 정합성을 유지 성공했습니다.**</br></br>
  
</div>
</details>

<details>
<summary><strong> 2⃣ Connection Pool Size 설정으로 데드락 문제 해결</strong></summary>
<div markdown="1">       
<br>
  
  - Pessimistic Lock은 데드락 발생 가능성이 있었습니다.
  - 데드락을 피하는 Connection Pool Size 공식과 JMeter 부하테스트를 통해 데드락을 회피할 수 있으며</br>에러율이 가장 낮은 지점(20)을 발견하고 적용하였습니다.
  - **Default Connection Pool Size인 10개 기준보다 에러율이 10% 하락했습니다.**</br>
  
</div>
</details>
<br/>


## 💉 프로젝트 관리
#### ✔ 애플리케이션 배포
- Github Actions + Elastic Beanstalk 사용으로 **CI/CD 환경을 구축**했습니다.
- ALB를 활용한 **Trigger 기반 오토 스케일링**으로 유동적인 트래픽에 효율적으로 대응하고 있습니다.
- Github Actions CI 동작 시 **Gradle 세팅 캐싱**을 통해 더욱 생산성을 높였습니다.
- 인터넷 보안 환경과 UX를 위해 **HTTPS**를 적용했습니다.
#### ✔ 모니터링
- Cloud Watch를 사용하여 **로그 + 성능 지표를 모니터링** 하고 있습니다.
- CPU가 70%를 초과하면 알림이 울리는 **경보 프로세스**를 구축했습니다. 
#### ✔ 테스트 커버리지 ?%
- 과감한 코드 리팩토링과 자신있는 배포를 위해서 ?% 미달성 시 배포가 되지 않도록 제한을 걸었습니다.
- 불필요한 프로덕션 코드를 전부 제거하고 모든 코드를 이해하고 싶었습니다.
- <details><summary><strong>📢 클린코드 中</strong></summary><div markdown="1">       <br/><pre>얼마만큼의 코드를 자동화한 단위 테스트로 계산해야 할까? 대답할 필요조차 없다.<br/> 모조리 다 해야 한다. 모.조.리! 100% 테스트 커버리지를 권장하냐고? 권장이 아니라 강력히 요구한다. <br/>작성한 코드는 한 줄도 빠짐없이 전부 테스트해야 한다. 군말은 필요 없다. ― 클린코드 (로버트 마틴 저)</pre></div></details>
#### ✔ React를 통한 클라이언트 코드 작성
- **Single Page Application** 구현으로 UX를 최대화 시키고자 했습니다.
- 백엔드 프로젝트지만 React 코드를 직접 작성하여 **실제 협업 프로세스**처럼 진행하고자 했습니다.   

<br/>

## 🎯 트러블 슈팅

<details>
<summary><strong>📌 조회/정렬 동작 시 두개의 Index가 적용되지 않는 이슈가 발생했습니다. </strong></summary>
<div markdown="1">       

#### ❗ 문제상황
  - Full-Text-Search 키워드 필터가 포함된 필터링 조회 동작 시 타임아웃이 발생했습니다.
  - 필터링 조회에서 정렬(조회순, 날짜순)은 필수적으로 이루어져야 하는데 Full-Text-Search 키워드 필터가 동작하면서 Full-Text-Index가 쿼리에 적용되었고, 이로인해 정렬 컬럼의 인덱스가 누락되어 sort에 부하가 발생했습니다.
  
#### 💡 Solution :
  - 필터링 조회 시 정렬 컬럼으로 인덱스를 사용하기 위해 키워드 검색은 contains문을 사용하였습니다.
  - 키워드만으로 검색이 이루어질 때는 Full-Text-Search가 동작 되도록 설정했습니다.
 
#### ✔ 결과
  - 키워드에 따른 속도 편차는 발생하지만 평균 500ms로 성능의 안정화를 이루었습니다. 
  - ( 약 11,900%의 성능향상 효과를 얻었습니다. ) </br>
  
</div>
</details>

<details>
<summary><strong>📌 5,000 건의 상품 데이터 Cache Warmup 동작 시 Redis Latency의 지연이 발생했습니다.</strong> </summary>
<div markdown="1">       

#### ❗ 문제상황
  - 상품 데이터의 빠른 조회와 DB 부하 분산을 위해 캐싱은 필수였습니다.
  - 하지만 TCP 기반으로 동작하는 Redis에 5,000 건의 데이터를 개별로 Input 하면서 Latency의 지연이 발생했습니다.
  - ( 카테고리별 상위 5,000건의 상품 데이터를 캐싱하여 사용하고 있습니다. )
  - <strong>Request +5000 ( Redis 요청 5000건 발생)</strong>
  - ![1313](https://user-images.githubusercontent.com/112923814/207049796-b844c15d-4fba-4342-a256-65c6d6d1733b.png)
  - ![nonepipe 5000-](https://user-images.githubusercontent.com/112923814/207048644-36273836-353b-48b5-b3be-dc19f1b232ad.png)


  
#### 💡 Solution : Redis Pipeline 구축
  - 작업의 단위를 직접 구축해서 요청이 가능해졌습니다. ( 다중 Insert 가능 )
  
#### ✔ 결과
  - 5,000건의 TCP 통신이 1건(+5000)으로 축소되었습니다.
  - <strong>Request +1 ( Redis 요청 1건 발생)</strong>
  - ![131313](https://user-images.githubusercontent.com/112923814/207049817-dc7d5da6-a4ee-4f4e-99a3-5f7e88b98c56.png)
  - ![pipe 5000_](https://user-images.githubusercontent.com/112923814/207049047-4a8b1c9f-3f94-4a1d-88a2-711e1b9b428b.png)



</div>
</details>

<details>
<summary><strong>📌 높은 트래픽 상황에도 상품이 클릭될 때마다 조회수 Update 쿼리가 동작했습니다.</strong></summary>
<div markdown="1">       
  
#### ❗ 문제상황
  - 높은 트래픽이 발생할 때 조회가 일어날 때마다 발생하는 Update 쿼리는 서버에 큰 무리가 있었습니다.
  - <strong>10초간 상품 상세 조회가 1만회 동작할 때 에러율이 62.31% 발생했습니다.</strong> 
  - ![10,000 view update1](https://user-images.githubusercontent.com/112923814/207050945-515b7aec-1999-4547-bbba-53dc37670325.png)
  - ![10,000 view update graph](https://user-images.githubusercontent.com/112923814/207050910-be5d0354-3d3a-4312-9077-b8db909638d2.png)


  
#### 💡 Solution : Cache Write Back
  - 조회수를 캐시에 모아 일정 주기 DB에 배치하는 프로세스를 구현했습니다.
  - 싱글쓰레드인 Redis의 특성상 Atomic하게 Increment를 처리할 수 있었습니다..
  - 조회 기능의 많은 I/O와 함께 발생하는 Update 쿼리를 컨트롤할 수 있었습니다..  
  
#### ✔ 결과
  - 클릭 시마다 발생했던 Update 쿼리를 1시간 주기로 일어나는 배치 작업으로 최적화가 이루어졌습니다.
  - <strong>동일 상황에 에러율 0%를 달성했습니다.</strong>
  - ![10,000 view redis1](https://user-images.githubusercontent.com/112923814/207050998-1e314ddd-4fee-49f4-9b76-157514757c0c.png)
  - ![10,000 view redis graph](https://user-images.githubusercontent.com/112923814/207051036-38937920-808d-4bf0-9414-2a4f4504a93c.png)



</div>
</details>

<details>
<summary><strong>📌 쿼리문 동작 시 cross join이 발생하여 성능 이슈가 발생했습니다. </strong></summary>
<div markdown="1">       

#### ❗ 문제상황
  - 조회 쿼리 동작 시 DB 로그에 cross join이 발생한 것을 확인했습니다.</br>
  ( cross join 은 카다시안 곱을 수행하여 join하기 때문에 너무 많은 데이터를 가져와 성능이 저하됩니다. )
  
#### 💡 Solution :
  - inner join 명시적으로 사용했습니다.
  - join을 명시적으로 사용하지 않은 쿼리문에서 자동으로 cross join이 발생되고 있었기 때문에 join이 필요한 테이블에 inner join을 작성하였습니다.
 
#### ✔ 결과
  - cross join으로 나가던 쿼리문이 inner join 바뀌었습니다.
  - 200만건 기준 필터링 조회 시 평균 8초, 성능 200%까지 개선되었습니다. </br>
</div>
</details>

<details>
<summary><strong>📌 AWS 인스턴스 성능 튜닝</strong></summary>
<div markdown="1">       

#### ❗ 문제상황
  - 스케일업 이전에 프리티어 인스턴스로 성능 최적화를 진행해보고자 했습니다.
  
#### 💡 Step 3
  
   1. <strong>DB 읽기 전용 복제본을 생성해 Read 요청을 분산합니다.</strong></br>
   2. <strong>Hikari Connection Pool 최적의 개수를 찾아야 했습니다.</strong></br>
   > Cache Write Back 전략으로 조회수를 관리하고 있었기에 Connection Pool 확장이 필요했습니다.
   > RDS micro.t3 인스턴스의 성능을 고려한 확장이 필요했습니다. </br>
   3. <strong>Time_Wait 소켓의 최적화가 필요했습니다.</strong></br>
   > 낮은 성능의 DB로 대규모 상품 데이터를 핸들링하는 상황이기에, 남아있는 모든 소켓에 요청마다
   > TCP handshake가 발생하는데에서 생기는 불필요한 성능 낭비를 없애야 했습니다.

  
#### ✔ 결과
  - Step 1. Main DB에는 Write 요청만을 동작시키고 Replica DB에 Read 동작을 분산 동작시켜 부하를 분산했습니다.
  - Step 2. Jmeter 부하테스트를 통해 에러율이 가장 낮아지는 Connection Pool Size가 20임을 발견했습니다.
  > Default Size인 10개에서 20개로 확장하니 1초 동안 이루어지는 동시 주문 150건 기준 에러율이 371% 하락했습니다.</br>
  > ( Error 21.04% -> Error  5.67% )  
  - Step 3. KeepAlive 적용을 통해 매 요청마다 새로운 세션을 만들지 않고, 1024개의 세션을 연결한 뒤 그 연결을 통해 요청을 처리하게 만들었습니다.

</div>
</details>

<br/>

## 🧑‍💻팀원

|송제윤|윤수영|계현준|
|:--|:--|:--|
|jy|sy|hj|

