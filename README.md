

# aibook
### 배포: https://aibook-front.netlify.app/home

[api 서버](https://github.com/kkkkimtaehyeon/aibook_api) - Java/SpringBoot

[ai 서버](https://github.com/kkkkimtaehyeon/aibook_ai) - Python/Fast API

[front 서버](https://github.com/kkkkimtaehyeon/aibook_front) - JavaScript/React
# 개발 동기

현대 사회에서 아이들이 스마트폰에 지나치게 의존해서 생기는 소통부족, 문해력 부족과 같은 문제점들을 보며, 이에 대한 해결방안을 고민했습니다. 

아이들이 부모와 함께 제작에 참여하여 부모와의 유대감을 키우고 자신만의 이야기를 창작할 수 있는 기회를 제공하게 하는 서비스를 개발하게 되었습니다.

### 기대효과

"아이북"은 간편한 인터페이스와 접근성을 통해 부모와 자녀가 함께 그림책을 만드는 과정을 더욱 즐겁고 유익하게 만들고자 합니다. 

이를 통해 많은 가정에서 어린이의 언어 및 어휘 발달을 촉진하고, 부모와 자녀간의 상호작용을 강화할 수 있을 것이라고 기대합니다.

# 서비스 소개
생성형 AI를 기반으로 나만의 동화책을 만드는 서비스 AiBook입니다. 사용자가 자신의 이야기를 작성하면 생성형 AI가 작성한 이야기를 기반으로 3개의 문장을 보여줍니다.

2024.07 SW중심대학 경진대회에서 출품한 **그린나래**라는 프로젝트을 설계, 보안, 기능적으로 보완하여 더 업그레이드 시켜보고 싶은 마음에 **아이북**이라는 프로젝트로 다시 개발을 진행합니다.

# 기능 소개

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

