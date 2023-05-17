# Group SNS (2023.04 ~ 진행중)

## 결과

[배포 사이트](http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8085/)
[Swagger Docs](http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8085/swagger-ui/)

## ERD

![](https://blog.kakaocdn.net/dn/pHIXY/btsfdczG1eL/T7FIL4E08Izm0QVe814Jwk/img.png)

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
  - 해당 그룹의 상세 정보(그룹명, 그룹 등급, 입장 코드, 게시글 수, 그룹원 수, 생성일) 조회
  - 그룹에 해당한 유저들의 정보(닉네임, 유저 등급, 그룹에 작성한 게시글 수) 조회
- 그룹 수정
  - 모든 그룹원들은 그룹 정보 수정 가능
  - 해당 그룹의 그룹명, 입장 코드 수정 가능
- 그룹 삭제
  - 모든 그룹원들은 그룹 삭제 가능
  - 그룹 삭제 시 그룹에 속한 스토리 모두 삭제
- 그룹 탈퇴
  - 유저가 그룹 탈퇴 시, 해당 유저가 작성한 스토리 모두 삭제
  - 그룹에 1명 있는데 탈퇴한 경우, 그룹 삭제

### 달력 기능

- 달력 페이지에서 그룹 선택 시 그룹 게시글이 달력에 출력
- 이전달, 다음달, 오늘(이번달) 이동 가능
- 달력 페이지에서 스토리 추가 가능
  - 스토리 작성 시 현재 선택한 그룹 자동으로 선택(변경 가능)

### 스토리 기능

- 스토리 추가
  - 그룹 선택, 날짜 선택, 공개 여부 선택 가능
  - 제목, 내용 입력 가능
  - 이미지는 최대 5개(개당 10MB 제한) 업로드 가능
- 스토리 조회
  - 달력 페이지에서 스토리 클릭 시 스토리 조회 가능
  - 그룹명, 제목, 작성자, 날짜, 내용 출력 및 이미지 출력
- 스토리 수정
  - 작성자가 아니라도 그룹의 유저라면 수정 가능
  - 스토리의 제목, 내용, 날짜, 이미지, 공개 여부 수정 가능
  - 이미지를 추가하지 않으면(null이면) 기존의 이미지 유지
- 스토리 삭제
  - 작성자가 아니라도 그룹의 유저라면 삭제 가능

### 등급 기능

- 유저 등급
  - 유저의 등급에 따라 참여할 수 있는 그룹의 수 제한
  - BRONZE : 3
  - SILVER : 5
  - GOLD : 10
  - DIAMOND : 제한 X
  - ADMIN : 제한 X
- 그룹 등급
  - 그룹의 등급에 따라 하루에 작성할 수 있는 스토리의 수, 하나의 스토리에 추가할 수 있는 이미지의 수, 그룹에 참여할 수 있는 유저의 수 제한
  - BRONZE : 1, 5, 5
  - SILVER : 3, 5, 10
  - GOLD : 3, 10, 20
  - DIAMOND: 10, 20, 50
- 최초 생성시 유저, 그룹 모두 BRONZE로 설정
- 추후에 결제 기능을 통해 등급 조정 기능 추가할 예정

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
- 그룹 수정 : PUT /api/groups/{groupId}
- 그룹 삭제 : DELETE /api/groups/{groupId}
- 그룹 탈퇴 : DELETE /api/groups/leave/{groupId}

### 스토리 기능 (/api/stories)

- 스토리 추가 : POST /api/stories
- 스토리 리스트 조회 : GET /api/stories/list
- 스토리 조회 : GET /api/stories/{storyId}
- 스토리 수정 : PUT /api/stories/{storyId}
- 스토리 삭제 : DELETE /api/stories/{storyId}

## 추가 예정

- 댓글 기능
- 다른 그룹 스토리 리스트 => 페이징, 검색 기능 추가
- 1대 1채팅(그룹 초대 메세지, DM)
- 알림(채팅, 스토리 추가)
- 결제(제한 숫자 증가)
- 관리자 기능
- Test Code 작성
