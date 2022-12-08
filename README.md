# 🐸 Spring Market
[![spring-market](https://github.com/SpringMarket/Market/actions/workflows/gradle.yml/badge.svg)](https://github.com/SpringMarket/Market/actions/workflows/gradle.yml)

## 프로젝트 소개


👉 [Spring Market 이용해보기 Click!](https://www.notion.so/1-3b015d8a07d149148b5fea36c4035ceb) <br>
👉 [Spring Market 팀 노션 Click!](https://www.notion.so/1-3b015d8a07d149148b5fea36c4035ceb)

## ⚙ InfraStructure
![InfraStructure](https://user-images.githubusercontent.com/112923814/206205534-6b2cf6e1-9461-4258-bbc1-f54b762be4b8.jpg)

# 🛠 Tech Stack
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

## 기술 스택 & 라이브러리 사용 이유

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

## 부하테스트
현준

## 트러블 슈팅
- 조회 성능 향상 (인덱스) 
1. 테이블 반정규화 
2. sorting 부하
3. full text search와 결합 인덱스 동시에 안 탐
4. RDS 스케일업과 DB replica

- 조회수 업데이트
-> Spring Batch
-> Redis

- 동시성 {
-> Pessimistic Lock 처리 오류
-> DB 스케일업
-> DB Replica
-> Max pool size custom
-> 트랜잭션 분리 Propagation.REQUIRES_NEW
-> saveAndFlush()
}

- 배포 시 DB 연결 성능 느림 600초 -> 6초 
- 메인페이지 성능 향상 - Redis Zset
- 레디스 파이프라인 -> 웜업 + ...

## 팀원

|송제윤|윤수영|계현준|
|:--|:--|:--|
|jy|sy|hj|

