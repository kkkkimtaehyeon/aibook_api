# AiBook
생성형 AI를 기반으로 나만의 동화책을 만드는 서비스 AiBook입니다.
사용자가 자신의 이야기를 작성하면 생성형 AI가 작성한 이야기를 기반으로 3개의 문장을 보여줍니다.

# TODO(common)
- [x] 공통된 응답형식 - ApiResonse
# TODO(Spring)
## Story
- [ ] Story(BaseStory)를 저장
- [ ] Page를 저장
- [ ] Story(제목, 공개여부)를 업데이트
- [ ] Story에 맞는 Tag를 저장
- [ ] public Story 목록을 조회 (비회원도 조회 가능)
- [ ] private Story 목록을 조회 (회원 본인만 조회 가능)
- [ ] Story를 검색
- [ ] Story를 정렬 (날짜, 조회수, 추천 등) 
- [ ] Story 상세 조회
- [ ] Story 삭제

## Member
- [x] 소셜 로그인 - 카카오
- [ ] 회원 정보 조회
- [x] 회원 가입
- [ ] 로그인 유지
- [ ] 로그아웃
- [ ] 권한별 접근제한
- [ ] 회원정보 수정
- [ ] 회원 탈퇴

## Authentication
- [x] AccessToken 발급
- [ ] RefreshToken 발급
- [ ] RefreshToken을 기반으로 AccessToken을 재발급
- [ ] JWT Filter
