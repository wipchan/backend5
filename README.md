# Backend5

이 프로젝트는 **백엔드 개발의 핵심 기술과 구조**를 직접 구현하고 실습하는 것을 목표로 합니다.  
실제 서비스에 적용되는 기능을 따라 만들어보며, **실무에 가까운 개발 경험**을 쌓을 수 있습니다.

---

## 📌 프로젝트 소개

- **회원 인증**, **게시판**, **댓글**, **채팅** 등 웹 서비스의 필수 기능을 직접 구현하며 백엔드 개발의 흐름을 익힙니다.
- **WebSocket, STOMP, Redis, Docker, Nginx** 등 실무에서 자주 사용되는 기술을 직접 실습합니다.
- 코드 구조, 설정 파일, 배포 환경까지 경험해보며 **개발-운영 전체 흐름**을 이해합니다.

---

## 📁 폴더 구조
```
backend5/
├── backendProject/ # 메인 백엔드 프로젝트
│ ├── src/
│ │ └── main/java/org/example/backendproject/
│ │ ├── auth/ # 회원가입, 로그인, 인증 처리
│ │ ├── board/ # 게시판 기능
│ │ ├── comment/ # 댓글 기능
│ │ ├── purewebsocket/ # 순수 WebSocket 구현
│ │ ├── stompwebsocket/ # STOMP 기반 WebSocket 구현
│ │ └── user/ # 사용자 정보 관리
│ ├── resources/
│ │ └── static/ # 정적 파일 (HTML, JS, CSS)
│ └── nginx/ # Nginx 설정 파일
├── CICD/ # Jenkins 등 CI/CD 설정
├── Data/ # DB 초기 데이터, 덤프 파일 등
├── volumes/ # Docker 볼륨 마운트용
└── README.md # 프로젝트 설명 문서
```

---

## 🧩 주요 기능

- ✅ 회원가입, 로그인, JWT 인증 처리
- ✅ 게시판 CRUD (글 등록, 수정, 삭제, 조회)
- ✅ 댓글 기능 (게시글별 연결 및 대댓글 처리)
- ✅ 실시간 채팅 기능
    - 순수 WebSocket 구현
    - STOMP + Redis Pub/Sub 기반 멀티 서버 지원
- ✅ Nginx 리버스 프록시 설정
- ✅ Docker, Docker Compose를 통한 개발/배포 환경 구성

---

## 🚀 실행 방법

### 1. Gradle 빌드

\```bash
./gradlew clean build
\```

### 2. 로컬 개발 서버 실행

\```bash
./gradlew bootRun
\```

- 기본 포트: `http://localhost:8080`
- 적용 설정: `application.properties` 또는 `application-dev.properties`

### 3. Docker로 전체 서비스 실행

\```bash
docker-compose -f backendProject/docker-compose.backend.yml up --build
\```

#### 포함된 컨테이너 목록
- `backend1`, `backend2`, `backend3`: Spring Boot 애플리케이션
- `mysql`: MySQL (포트 3307 → 내부 3306)
- `redis`: Redis (포트 6379)
- `nginx`: Nginx (포트 80)

> ⚠️ `.env` 또는 `application.properties`에 다음 항목들이 올바르게 설정되어 있어야 합니다:
> - `DB_SERVER`, `DB_PORT`, `DB_USER`, `DB_PASS`
> - `REDIS_HOST`
> - `OPEN_API_KEY`, `OPEN_MODEL`

> 💡 WebSocket 및 채팅 기능을 사용하려면 Redis 컨테이너가 **먼저 실행**되어 있어야 정상 작동합니다.
