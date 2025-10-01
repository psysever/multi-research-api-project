# Multi-Research API Project

## 📥 PORTFOLIO

### https://jackwork.work

<br>

## 📊 Research's GitHub Stats

![GitHub stats](https://github-readme-stats.vercel.app/api?username=psysever&show_icons=true&theme=dracula)

> **Note**: GitHub stats are updated periodically (not in real-time) due to API caching. Statistics may take several
> hours to reflect recent activity.

## 🚀 Project Overview

🇺🇸 (ENG)
A comprehensive API project that consolidates core functionalities developed through extensive trial and error across
numerous projects and research endeavors.

<br>
🇰🇷 (KOR)
수많은 프로젝트를 진행하면서 겪었던 시행착오와 연구를 통해 완성된 핵심 기능들을 모아놓은 종합 API 프로젝트입니다.
<br>

### 📁 Project Structure

```
multi-api-project/
│── multi-research1-api/     # First research API module
│    └── domain
│        │── admin           # JPA
│        │── file            # AWS S3    
│        └── social          # Oauth      
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

## 🛠️ Technology Stack

## 📋 Prerequisites

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

## 📥Installation

## ▶️ Running the Application

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

## 📄 License

## 🤝 Contributing



