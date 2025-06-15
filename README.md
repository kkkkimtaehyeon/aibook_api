# AiBook
생성형 AI를 기반으로 나만의 동화책을 만드는 서비스 AiBook입니다.
사용자가 자신의 이야기를 작성하면 생성형 AI가 작성한 이야기를 기반으로 3개의 문장을 보여줍니다.

2024.07 SW중심대학 경진대회에서 출품한 "그린나래"라는 프로젝트을 설계, 보안, 기능적으로 보완하여 더 업그레이드 시켜보고 싶은 마음에
"아이북"이라는 프로젝트로 다시 개발을 진행합니다.

# 핵심기능 목록
- [x] 선택지에 따라 유기적인 동화를 생성 기능
- [ ] 동화 내용에 맞는 삽화를 생성 기능
- [x] 동화 내용을 TTS로 읽어주는 기능
- [x] 동화를 직접 더빙하는 기능
- [x] 부모의 목소리를 voice cloning하여 TTS로 동화를 읽어주는 기능


# TODO(common)
- [x] 공통된 응답형식 - ApiResonse
# TODO(Spring)
## Story
- [x] Story(BaseStory)를 저장
- [x] Page를 저장
- [x] Story(제목, 공개여부)를 업데이트
- [ ] Story에 맞는 Tag를 저장
- [x] public Story 목록을 조회 (비회원도 조회 가능)
- [x] private Story 목록을 조회 (회원 본인만 조회 가능)
- [x] Story를 검색
- [x] Story를 정렬 (날짜, 조회수, 추천 등)
- [x] Story 상세 조회
- [x] Story 삭제

## Member
- [x] 소셜 로그인 - 카카오
- [x] 회원 정보 조회
- [x] 회원 가입
- [x] 로그인 유지
- [x] 로그아웃
- [ ] 권한별 접근제한
- [ ] 회원정보 수정
- [ ] 회원 탈퇴

## Authentication
- [x] AccessToken 발급
- [x] RefreshToken 발급
- [x] RefreshToken을 기반으로 AccessToken을 재발급
- [x] JWT Filter


# 패치노트
25.02.18 - 조회수 추가

25.02.19 - 동화 더빙 기능 추가

25.02.20 - 동화 검색/정렬 기능 추가

25.03.19
- 목소리 등록 로직 수정(내 최근 동화를 조회하고 없으면 동화 먼저 생성하도록, 있으면 그 동화 내용으로 보이스 클로닝)
- 동화 더빙 전용 페이지 구현 (보이스 클로닝 더빙, 직접 더빙)
- 마이페이지/동화 목록 화면 비율 수정
- 새로고침 시 로그아웃처리 되는 현상 수정
- 페이지별 접근 제한 설정
- GPT 동화 내용 생성 퀄리티 개선

25.03.28
- 더빙 음성은 자신이 더빙한 동화만 보여주도록 수정
- 더빙 추가할 동화 "내 동화", "전체 동화"로 구분
- Header에 사용자 이름 없어지는 것, 마이페이지 비율 깨지는 것, 목소리 목록 크기 수정
- 더빙 완료 시 알림
- storyDubbing, storyDubbingPage 엔티티 추가 (더빙하면 기존 동화를 복제하는 개념)

25.04.22
- 동화 생성 로직 수정
- 동화 생성 퀄리티 향상
  - story context 추출
  - 생성 요청 시 story context와 story history로 맥락 유지
  - story arc(발단, 전개..) 추가
  - 프롬프트 수정
- access token 재발급 구현, refresh token은 쿠키, redis에 저장
- 목소리 목록에서 목소리(or 보이스 클로닝된 샘플) 재생
- 회원탈퇴
- 입력 도우미 구현

25.04.29
- 토큰 만료 시 로그아웃 안되는 오류 해결
- 목소리 삭제

25.05.10
- 더빙 로직 수정
- api 서버
  - preSignedUrl(put) 발급
  - 응답 받은 오디오 url db 저장
- ai 서버
  - zyphra api 오류 수정
  - preSignedUrl에 오디오 데이터 업로드
  - preSignedUrl에서 쿼리스트링 제거한 url(오디오 다운로드 url) 응답

25.05.14
- 회원가입 페이지 수정

25.05.26
- 좋아요 개수 오류 수정
- 좋아요 정렬 오류 수정
- 외부 API 재시도 로직 구현
- Front-end
  - 새 동화 만드는 페이지 수정
  - 새로고침 시 재렌더링 방지

25.05.27
- stable diffusion api로 이미지 생성 기능 구현
- 동화 표지 추가로 인한 동화 저장 로직 수정
- Front-end
  - 동화 표지 생성 페이지 구현

25.05.28
- 동화 내용 생성 퀄리티 향상(프롬프트 엔지니어링)
  25.05.29
- fastapi 인터페이스, 클래스 재정의
- 마이페이지에 들어가야만 토큰 재발급 로직 수행되는 것 수정

25.06.13
- ai-server
  - story, image 서비스 코드 리팩토링 (확장성 있게)
- api-server
  - 로그인 시 리프레시 토큰 Redis 저장 예외처리
  - 회원가입 validation 강화, 예외 메시지 구체화

25.06.14
- **zonos api 오류 수정**
- 오류 원인: MediaRecorder로 webm 오디오를 녹음해서 사용하고 있었는데, zonos는 webm을 지원하지 않는 것으로 예상(손실이 크기 때문에)
    - RecordRTC 사용, wav로 녹음 
      - spring 기존 단일 multiParFile 크기는 1MB, 5MB로 수정(이유: 30초 분량 여유분) 
    - s3 의존성 변경 [org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE] -> [software.amazon.awssdk:s3:2.31.62]
      - 업로드 파일 content type 지정을 위한 PutObjectRequest 사용 위함.