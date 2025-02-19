# AiBook
생성형 AI를 기반으로 나만의 동화책을 만드는 서비스 AiBook입니다.
사용자가 자신의 이야기를 작성하면 생성형 AI가 작성한 이야기를 기반으로 3개의 문장을 보여줍니다.

2024.07 SW중심대학 경진대회에서 출품한 "그린나래"라는 프로젝트을 설계, 보안, 기능적으로 보완하여 더 업그레이드 시켜보고 싶은 마음에 
"아이북"이라는 프로젝트로 다시 개발을 진행합니다.

# 핵심기능 목록
- [x] 선택지에 따라 유기적인 동화를 생성 기능
- [ ] 동화 내용에 맞는 삽화를 생성 기능
- [ ] 동화 내용을 TTS로 읽어주는 기능
- [x] 동화를 직접 더빙하는 기능
- [ ] 부모의 목소리를 voice cloning하여 TTS로 동화를 읽어주는 기능


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
- [ ] Story를 검색
- [ ] Story를 정렬 (날짜, 조회수, 추천 등) 
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
- [ ] RefreshToken 발급
- [ ] RefreshToken을 기반으로 AccessToken을 재발급
- [x] JWT Filter


# 패치노트
25.02.18 - 조회수 추가

25.02.19 - 동화 더빙 기능 추가
