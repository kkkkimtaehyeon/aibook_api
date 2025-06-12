

# aibook
### 배포 
https://aibook-front.netlify.app/home
### 레포지토리
[api 서버](https://github.com/kkkkimtaehyeon/aibook_api) - Java/SpringBoot

[ai 서버](https://github.com/kkkkimtaehyeon/aibook_ai) - Python/Fast API

[front 서버](https://github.com/kkkkimtaehyeon/aibook_front) - JavaScript/React
# 개발 동기

아동의 문해력 저하와 가족 간 소통 부족 문제를 기술로 해결하고자 
"아이북"을 개발했습니다.

GPT를 활용한 문장 생성과 인터랙티브한 이야기 전개를 통해 아이의 
문해력과 사고력을 자연스럽게 키우고, 보이스 클로닝으로 부모의 목소리로 동화를 들려주며 정서적 교감을 유도합니다.
기술 기반의 창의적 학습과 따뜻한 가족 경험을 동시에 실현하는 것을 목표로 했습니다.

### 기대효과

"아이북"은 간편한 인터페이스와 접근성을 통해 부모와 자녀가 함께 그림책을 만드는 과정을 더욱 즐겁고 유익하게 만들고자 합니다. 

이를 통해 많은 가정에서 어린이의 언어 및 어휘 발달을 촉진하고, 부모와 자녀간의 상호작용을 강화할 수 있을 것이라고 기대합니다.

# 서비스 소개
생성형 AI를 기반으로 나만의 동화책을 만드는 서비스 AiBook입니다. 사용자가 자신의 이야기를 작성하면 생성형 AI가 작성한 이야기를 기반으로 3개의 문장을 보여줍니다.

2024.07 SW중심대학 경진대회에서 출품한 **그린나래**라는 프로젝트을 설계, 보안, 기능적으로 보완하여 더 업그레이드 시켜보고 싶은 마음에 **아이북**이라는 프로젝트로 다시 개발을 진행합니다.

# 주요 기능 소개
### 동화 만들기
<img src="https://github.com/user-attachments/assets/307ee4da-149a-445b-b9b0-f9d6ef8e6c07" width="200" height="450"/>
<img src="https://github.com/user-attachments/assets/9074b6e5-79d4-4623-97a8-1c683355d86f" width="200" height="450"/>
<img src="https://github.com/user-attachments/assets/38e0f3dd-2612-43f8-b825-2fd9aba04bb3" width="200" height="450"/>

### 동화 더빙
<img src="https://github.com/user-attachments/assets/4c4c29c8-1357-4633-83f1-753cccfa99ca" width="200" height="450"/>
<img src="https://github.com/user-attachments/assets/ed991fdd-16df-4cc0-92ba-7cfc60c9da83" width="200" height="450"/>


# 개발 환경
### Backend

- API 서버
    - Language: jdk eclipse temurin 21
    - Framework: springboot 3.3.6
    - DB: mysql, redis
    - ORM: JPA, QueryDsl
    - auth:  JWT, OAuth 2.0
- AI 서버
    - Language: python 3.12.4
    - Framework: FastApi
    - DB: redis
### Frontend
- Language: javascript, html, css
- Framework: react (vite)
- etc: bootstrap

### Infra
- Cloud: AWS EC2 (ubuntu), AWS RDS(MySQL), AWS Elasticache(valkey), AWS S3
- Container: Docker, Docker Compose
- CI/CD: Github Action

## 아키텍처
![아키텍처](https://github.com/user-attachments/assets/7ded3663-b4ab-4225-9772-753f9f29ace3)

