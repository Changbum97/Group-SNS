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

## Api End Points

- [Swagger Docs](http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8085/swagger-ui/)
- 유저 기능 (/api/users)
  - 회원가입 : POST /api/users/join
  - 로그인 : POST /api/users/login
    - 로그인 시 AccessToken, RefreshToken 부여
    - Swagger 페이지의 Authorize에 'Bearer' + Token을 넣어줌으로써 로그인 가능
  - 로그인 아이디 중복 체크 : GET /api/users/check-loginId
  - 닉네임 중복 체크 : GET /api/users/check-nickname
  - 이메일 중복 체크 : GET /api/users/check-email
  - Access Token 재발급 : GET /api/usrs/access-token
    - Refresh Token이 올바른 경우에만 Access Token 발급
  - 로그인 테스트 : GET /api/users/test


## 기능

- 결제
- 1대1 채팅
- Redis
- 인증
- 알람
- Oauth
- 관리자 기능
- Controller, RestController 분리
