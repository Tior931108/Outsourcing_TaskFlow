# Outsourcing_TaskFlow 백엔드 팀 프로젝트 과제

## 📌 프로젝트 소개
본 프로젝트는 기업용 태스크 관리 시스템 (TaskFlow)을 주제로 한 아웃소싱 프로젝트입니다. <br>
예시로 제작된 프론트엔드 결과물에 맞춰 백엔드 API 서버를 구현했습니다.
모든 API는 사전에 정의된 API 명세서와 일치하도록 작성했으며, 공용 응답 객체를 사용해 통일된 형식으로 결과 값을 반환하도록 구현했습니다.
<br/>
<br/>

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

<br>

---

## 📱 주요 기능

### 👤 사용자 관리
- 회원가입 / 로그인
- JWT 기반 인증
- 사용자 프로필 조회
- 사용자 정보 수정
- 회원탈퇴

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
<br>

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

<br>

## 🗓 개발 기간
- 2025.12.09 ~ 2025.12.13
<br>

## 🗂 ERD
<img width="1700" height="902" alt="image" src="https://github.com/user-attachments/assets/2eaf3bf9-dffd-4c28-86c1-8da86732fd65" />
<br>

<br>

## 📘 API 명세서
👉 [API 명세서 바로가기](https://teamsparta.notion.site/TaskFlow-API-2c32dc3ef51481139566e0201d71fe44) <br><br>
<img width="706" height="656" alt="image" src="https://github.com/user-attachments/assets/2b849b47-0991-494c-8b5c-353a96c8bced" />
<img width="705" height="745" alt="image" src="https://github.com/user-attachments/assets/ee55614f-a9c3-4e3f-8a4b-cea1a2aea147" />

<br><br>

## 👤 개발자
Spring 심화 팀 프로젝트 8조 팔방미인즈..^^ 정용준, 김동욱, 임정하, 최정윤, 김재환
