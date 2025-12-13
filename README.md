# TaskFlow 백엔드 팀 프로젝트 과제

## 📌 프로젝트 소개
본 프로젝트는 부트캠프 


## 🛠 기술 스택

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.5
- **ORM**: Spring Data JPA (Hibernate)
- **Database**: MySQL
- **Security / Auth**: Spring Security, JWT
- **Validation**: Jakarta Validation
- **Logging**: AOP
- **Build Tool**: Gradle
- **IDE**: IntelliJ IDEA

### Frontend
- **Runtime**: Docker
- **Framework**: Provided Frontend (Client)
- **Port**: `localhost:3000`

### Dev / Tool
- **API Test**: Postman
- **Version Control**: Git, GitHub


---

## 📱 주요 기능

### 👤 사용자 관리
- 회원가입 / 로그인
- JWT 기반 인증
- 사용자 프로필 조회

### 📋 작업 관리
- 작업 생성 / 조회 / 수정 / 삭제
- 작업 상태 관리
- 댓글을 통한 협업 기능

### 👥 팀 관리
- 팀 생성 및 관리
- 팀 멤버 관리
- 권한에 따른 접근 제어

### 📺 활동 로그
- 사용자 활동 내역 기록 및 조회

### 📊 대시보드 & 검색
- 대시보드용 통계 데이터 제공
- 통합 검색 기능

---

## 🔐 인증 및 보안
- JWT 기반 인증 / 인가
- Spring Security 커스터마이징
- 비밀번호 BCrypt 암호화
- 사용자 권한(Role)에 따른 접근 제어

---

## ⚙️ 비기능 요구사항
- 공통 응답 포맷 적용
- 전역 예외 처리
- CORS 설정
  - 개발 환경: `http://localhost:3000`

---

## 🐳 실행 방법

### 1️⃣ 프론트엔드 실행
```bash
docker pull [frontend-image]
docker run -p 3000:3000 [frontend-image]
```

### 2️⃣ 백엔드 실행
```bash
./gradlew bootRun
```


## 🗓 개발 기간
- 2025.11.07 ~ 2025.11.20
<br>

## 🗂 ERD

<img width="1700" height="902" alt="image" src="https://github.com/user-attachments/assets/2eaf3bf9-dffd-4c28-86c1-8da86732fd65" />
<br>



## 📘 API 명세서
Postman API 문서를 통해 각 API의 요청/응답 예시를 확인할 수 있습니다.  
👉 [API 명세서 바로가기](https://documenter.getpostman.com/view/47338059/2sB3WyKGKL)
<br><br>

