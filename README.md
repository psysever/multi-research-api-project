# Multi-Research API Project

## 📥 PORTFOLIO

### https://jackwork.work

<br>

## 📊 Research's GitHub Stats

![GitHub stats](https://github-readme-stats.vercel.app/api?username=psysever&show_icons=true&theme=dracula)

> **Note**: GitHub stats are updated periodically (not in real-time) due to API caching. Statistics may take several
> hours to reflect recent activity.

## 🚀 Project Overview

🇺🇸 (ENG)<br />

1. A comprehensive API project that consolidates and simplifies core functionalities developed through extensive trial
   and error across numerous projects and research endeavors.<br />
2. While there are many more complex business logics in actual implementation, this project focuses on demonstrating
   simplified functional aspects for clarity and understanding.<br />
3. Please note that domain model structures (DB schemas), repositories, and MyBatis queries cannot be fully disclosed
   due to security policies from my previous workplace.<br />

<br>
🇰🇷 (KOR)<br /><br />
1. 수많은 프로젝트를 진행하면서 겪었던 시행착오와 연구를 통해 완성된 핵심 기능들을 간소화하여 모아놓은 종합 API 프로젝트입니다.<br />
2. 실제 구현에는 더 많고 복잡한 비즈니스 로직이 있지만, 이해하기 쉽도록 기능 위주의 간소화된 내용으로 구성하였습니다.<br />
3. 도메인 모델 구조(DB 스키마)와 레포지토리, MyBatis 쿼리는 종사했던 곳의 보안 정책으로 인해 모두 공개하지 못한 점 양해 부탁드립니다.<br />
<br>

### 📁 Project Structure

```
multi-api-project/
│── multi-research1-api/     # First research API module
│    └── domain
│        │── file            # JPA
│        │── jpa             # AWS S3    
│        └── oauth           # Oauth      
│
│── multi-research2-api/     # Second research API module
│    └── domain
│       │── auth                 # With redis
│       │── chat                 # Socket    
│       │── cipher               # Decryption Encryption   
│       │── db-master-slave      # Write DB, Read DB
│       │── excel                # Excel
│       │── fcm_push_message     # Fcm push message
│       │── gpt                  # Create response content with gpt
│       └── transaction          # Transaction type
│
│           
│── build.gradle            # Root build configuration
└── settings.gradle         # Multi-module settings
```

# 1.multi-research1-api

## 1.1 domain - file

### CONTENT LINK

https://jackwork.work/works/48?type=RESEARCH&page=1
<br />

🇺🇸 (ENG)<br />
Implements AWS S3 file upload functionality with security-based access control using S3 IAM policies to handle public
and private file access permissions.

<br />
🇰🇷 (KOR)<br />
AWS S3 업로드 및 업로드된 파일의 S3 IAM 정책을 통한 public/private 보안 분기 처리
<br />
<br />

## 1.2 domain - jpa

### CONTENT LINK

https://jackwork.work/works/49?type=RESEARCH&page=1
<br />

🇺🇸 (ENG)<br />
Mastering JPA usage patterns and implementing comprehensive CRUD operations using JPA framework.

<br />
🇰🇷 (KOR)<br />
JPA 사용법 숙지 및 JPA를 활용한 CRUD 구현
<br />
<br />

## 1.3 domain - oauth

### CONTENT LINK

https://jackwork.work/works/50?type=RESEARCH&page=1
<br />

🇺🇸 (ENG)<br />
Implementing OAuth authentication integration with major platforms (Google, Facebook, TikTok, etc.) to retrieve user
profiles and channel information.

<br />
🇰🇷 (KOR)<br />
Google, Facebook, TikTok 등의 OAuth 인증을 통한 사용자 정보 및 채널 정보 수집
<br />
<br />

# 2.multi-research2-api

## 2.1 domain - auth

### CONTENT LINK

https://jackwork.work/works/51?type=RESEARCH&page=1
<br />

🇺🇸 (ENG)<br />
Implementing token-based authentication using Redis for ACCESS_TOKEN and REFRESH_TOKEN management. When ACCESS_TOKEN
expires, a new ACCESS_TOKEN is issued using the REFRESH_TOKEN.

<br />
🇰🇷 (KOR)<br />
Redis를 활용한 ACCESS_TOKEN 및 REFRESH_TOKEN 발행 시스템. ACCESS_TOKEN 만료 시 REFRESH_TOKEN을 통한 새로운 ACCESS_TOKEN 발행
<br />
<br />

## 2.2 domain - chat

### CONTENT LINK

https://jackwork.work/works/52?type=RESEARCH&page=1
<br />

🇺🇸 (ENG)<br />
Implementing real-time chat functionality using MongoDB,WebSocket and STOMP protocol for seamless messaging experience.

<br />
🇰🇷 (KOR)<br />
MongoDB,WebSocket과 STOMP 프로토콜을 활용한 실시간 채팅 기능 구현
<br />
<br />

## 2.3 domain - cipher

### CONTENT LINK

https://jackwork.work/works/53?type=RESEARCH&page=1
<br />

🇺🇸 (ENG)<br />
Implementing comprehensive encryption/decryption functionality:<br />

1. Frontend encrypts payload using RSA public key provided by backend controller before transmission<br />
2. Backend decrypts RSA-encrypted payload and stores data in database using AES encryption<br />
3. When frontend requests data, backend decrypts AES-encrypted database data and sends it to frontend<br />

<br />
🇰🇷 (KOR)<br />
종합적인 암호화/복호화 기능 구현:<br /><br />
1. 프론트엔드는 백엔드 컨트롤러에서 제공한 공개키로 RSA 방식을 사용하여 페이로드를 암호화한 후 백엔드로 전송<br />
2. 백엔드는 RSA 방식으로 암호화된 페이로드를 복호화한 후 AES를 통해 DB에 데이터를 암호화하여 저장<br />
3. 프론트엔드에서 데이터 요청 시 백엔드는 AES로 암호화된 DB 데이터를 복호화한 후 프론트엔드로 전송<br />
<br />
<br />

## 2.4 domain - db-master-slave

### CONTENT LINK

https://jackwork.work/works/54?type=RESEARCH&page=1<br />
<br />

🇺🇸 (ENG)<br />
Implementing database read/write separation architecture. Considering database overload and locking issues, separating
write and read operations using MASTER (write) and SLAVE (read) database configuration.

<br />
🇰🇷 (KOR)<br />
DB 읽기/쓰기 분리 아키텍처 구현. DB 과부하 및 잠금 문제를 고려하여 WRITE-READ DB를 분기 처리한 후 MASTER(쓰기), SLAVE(읽기) DB로 운영
<br />
<br />

## 2.5 domain - excel

### CONTENT LINK

https://jackwork.work/works/55?type=RESEARCH&page=1<br />
<br />

🇺🇸 (ENG)<br />
Implementing large dataset Excel file segmentation for efficient transmission:

1. Performance degradation occurs when downloading Excel files with 10,000+ records from frontend to backend<br />
2. Using pageSize variable to set maximum row limits, segmenting data into multiple Excel files for optimized delivery
   to frontend<br />

<br />
🇰🇷 (KOR)<br />
대용량 데이터 엑셀 파일 분할 전송 구현:<br /><br />
1. 프론트엔드에서 백엔드로 엑셀 다운로드 요청 시 10,000건 이상의 데이터는 성능 저하 발생<br />
2. pageSize 변수로 행의 최대값을 설정하여 데이터를 분할 처리한 후 여러 개의 엑셀 파일로 나누어 프론트엔드에 전송<br />
<br />
<br />

## 2.6 domain - fcm_push_message

### CONTENT LINK

https://jackwork.work/works/56?type=RESEARCH&page=1<br />
<br />

🇺🇸 (ENG)<br />
Implementing mobile app push notification system using Firebase Cloud Messaging (FCM) for real-time user engagement.

<br />
🇰🇷 (KOR)<br />
Firebase를 활용한 모바일 앱 푸시 메시지 시스템 구현
<br />
<br />

## 2.7 domain - gpt

### CONTENT LINK

https://jackwork.work/works/57?type=RESEARCH&page=1<br />
<br />

🇺🇸 (ENG)
Implementing GPT API integration functionality:<br />

1. Sending user health data and prompts to GPT model to generate personalized health reports

<br />
🇰🇷 (KOR)
GPT API 연동 기능 구현:<br />
1. 사용자의 건강 정보 데이터와 프롬프트를 GPT 모델에 전송하여 개인화된 건강 리포트 생성
<br />
<br />

## 2.8 domain - transaction

### CONTENT LINK

https://jackwork.work/works/58?type=RESEARCH&page=1<br />
<br />

🇺🇸 (ENG)<br />
Implementing various transaction management patterns and exploring different transaction types for data consistency and
integrity.

<br />
🇰🇷 (KOR)<br />
다양한 트랜잭션 관리 패턴 구현 및 데이터 일관성과 무결성을 위한 트랜잭션 유형 연구
<br />
<br />

## 2.9 domain - kafka

### CONTENT LINK

https://jackwork.work/works/59?type=RESEARCH&page=1<br />
<br />

🇺🇸 (ENG)<br />
Building Reliable Order Processing with Kafka:
Transactional Boundaries and Idempotent Consumers

<br />
🇰🇷 (KOR)<br />
Kafka를 활용한 신뢰성 있는 주문 처리 시스템 구축:
트랜잭션 경계와 멱등 컨슈머 설계
<br />
<br />

## 🔧 Dependencies

### Core Dependencies

- `mybatis-spring-boot-starter:3.0.3`
- `mysql-connector-j`
- `spring-boot-starter-data-redis`

### Security & JWT

- `spring-boot-starter-security`
- `jjwt-api:0.11.2`
- `jjwt-impl:0.11.2`
- `jjwt-orgjson:0.11.2`

### API Documentation

- `springfox-swagger-ui:3.0.0`
- `springfox-swagger2:3.0.0`
- `springfox-spring-web:3.0.0`
- `io.swagger.core.v3:2.2.21`

### Development Mode

```bash
./gradlew bootRun
```

### Using JAR File

```bash
java -jar build/libs/multi-research-api-0.0.1-SNAPSHOT.jar
```

## 🧪 Testing

Run the test suite:

```bash
./gradlew test
```

## 📚 API Documentation

Once the application is running, access the Swagger UI at:

- `http://localhost:8080/docs/swagger-ui/index.html`

## 🏗️ Build Information

- **Build Tool**: [Gradle](https://gradle.org/) - Dependency Management
- **Version**: MULTI_RESEARCH_API_V1
- **Artifact**: `multi-research-api-0.0.1-SNAPSHOT.jar`




