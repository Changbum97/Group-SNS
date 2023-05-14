# Group SNS

## 결과

[배포 사이트](http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8085/)
[Swagger Docs](http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8085/swagger-ui/)

## 개발 환경

- Language: JAVA 11
- Framework: SpringBoot 2.7.11
- Build: Gradle 7.6.1
- Server: AWS EC2
- Deploy: Docker
- CI/CD: Github Actions
- Database: MySQL 8.0
- File Storage: AWS S3

## 구현 기능

### 유저 기능

- 회원가입
  - 아이디, 닉네임, 이메일 중복 체크
  - 비밀번호, 비밀번호 확인이 같은지 체크
  - 이메일 인증 성공해야 가입 가능
- 로그인
  - 아이디, 비밀번호로 로그인
  - OAuth 로그인 (구글, 카카오) 가능
  - OAuth 로그인 가입 시 닉네임 설정 필수
  - 로그인 성공 시 쿠키에 AccessToken, RefreshToken 저장
- 아이디 찾기, 비밀번호 찾기 구현
  - 회원가입 시 등록했던 이메일로 아이디, 비밀번호 찾기
    
### 그룹 기능

- 그룹 생성
  - 그룹명 중복 체크
  - 그룹명과 입장 코드 입력을 통해 그룹 생성
  - 그룹 생성 시 생성 유저는 바로 그룹 참여
- 그룹 참여
  - 그룹명, 입장 코드가 일치하면 그룹 참여
  - 한 유저는 최대 3개의 그룹에 참여 가능
- 그룹 리스트
  - 로그인 한 유저가 속한 그룹의 리스트 출력
- 그룹 상세 정보 조회
  - 해당 그룹의 상세 정보(그룹명, 입장코드, 게시글 수, 그룹원 수, 생성일) 조회
  - 그룹에 해당한 유저들의 정보(닉네임, 권한, 그룹에 작성한 게시글 수) 조회

### 달력 기능

- 달력 페이지에서 그룹 선택 시 그룹 게시글이 달력에 출력
- 이전달, 다음달, 오늘(이번달) 이동 가능

### 스토리 기능

- 스토리 추가
  - 그룹 선택, 날짜 선택, 공개 여부 선택 가능
  - 제목, 내용 입력 가능
  - 이미지는 최대 5개(개당 10MB 제한) 업로드 가능
- 스토리 조회
  - 달력 페이지에서 스토리 클릭 시 스토리 조회 가능
  - 그룹명, 제목, 작성자, 날짜, 내용 출력 및 이미지 출력

## Api End Points

- [Swagger Docs](http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8085/swagger-ui/)

### 유저 기능 (/api/users)

- 회원가입 : POST /api/users/join
- 로그인 : POST /api/users/login
  - 로그인 시 AccessToken, RefreshToken 부여
  - Swagger 페이지의 Authorize에 'Bearer' + Token을 넣어줌으로써 로그인 가능
- 로그인 아이디 중복 체크 : GET /api/users/check-loginId
- 닉네임 중복 체크 : GET /api/users/check-nickname
- 이메일 중복 체크 : GET /api/users/check-email
- 이메일 인증번호 전송 : GET /api/users/send-auth-email
  - 회원가입 시 이메일을 인증하기 위해 사용
- 이메일 인증번호 검증 : GET /api/users/check-email-auth
  - 이메일 인증번호가 맞는지 체크
- 아이디 찾기 : GET /api/users/find-id
  - 가입했던 이메일로 아이디 전송
- 비밀번호 찾기 : GET /api/users/find-pw
  - 가입했던 이메일로 새로운 비밀번호 전송 및 유저 비밀번호 변경
- Access Token 재발급 : GET /api/usrs/access-token
  - Refresh Token이 올바른 경우에만 Access Token 발급
- 로그인 테스트 : GET /api/users/test

### 그룹 기능 (/api/groups)

- 그룹 추가 : POST /api/groups
- 그룹 참여 : GET /api/groups/join
- 내가 속한 그룹 리스트 : GET /api/groups/list
- 그룹 상세 정보 : GET /api/groups/{groupId}
- 그룹명 중복 체크 : GET /api/groups/check-name

### 스토리 기능 (/api/stories)

- 스토리 추가 : POST /api/stories
- 스토리 리스트 조회 : GET /api/stories/list
- 스토리 조회 : GET /api/stories/{storyId}

## 추가 예정

- 스토리에 댓글 기능
- 다른 그룹 스토리 조회
- 달력 열거형 리스트 출력
- 그룹 별 최대 인원 수 제한
- 그룹, 날짜 별 최대 스토리 수 제한
- 1대 1채팅(그룹 초대 메세지)
- 알림(채팅, 스토리 추가)
- 결제(제한 숫자 증가)
- 관리자 기능
- Test Code 작성
