# 게시판 만들기

본 문서는 농수산물 직거래 게시판 만들기 프로젝트의 요구사항을 정리한 문서입니다.

## 프로젝트의 목적

취업 포트폴리오를 위한 개인 프로젝트를 만들어 본다.

웹의 기본 소양이 되는 CRUD 게시판을 만들고, 기능을 하나씩 추가해 나가는 식으로 진행한다.(추가되는 기능은 추후에 업데이트)

## 프로젝트 구조

<!-- ![img.png](img/architecture.png) -->

## 사용 기술스택

- SpringBoot
- Java
- Mariadb
- Spring Security
  (추후 기술 스택 추가 예정)

## 프로젝트의 기능

### 1단계(기본 기능)

1. 회원가입
    - 유효성 검사, 중복 검사
        1. 회원 아이디 o
        2. 회원 닉네임 o
        3. 회원 비밀번호 o
        4. 회원 주소 
        5. 회원 휴대폰번호 o
        6. 이메일 인증 o


2. 로그인
    - 아이디 찾기, 비밀번호 초기화 기능 
    - 로그인 상태에서 글, 댓글 작성 가능 
    - 회원 정보 수정 가능 o


3. 회원 정보 수정
    - 비밀번호, 주소, 휴대폰 수정 가능 o
    - 기존 비밀번호와 다르면 수정 불가능 o


4. 글 CRUD
    - 로그인 시 글 작성
    - 자신이 쓴 글 수정 가능
    - 조회수, 좋아요 추가
    - 검색 태그


5. 댓글 CRUD
    - 로그인시 댓글 작성 가능
    - 자신이 쓴 댓글 수정 가능

### 2단계 (추가 세부 기능)

6. 게시글 페이징 처리


7. 게시글 검색 기능


8. 소셜 로그인 기능 추가
    - 네이버, 구글, 카카오 소셜 로그인


9. 결제 확인
    - 구매자가 판매자에게 송금한 사실을 인증
    - 입금이 돠었다는 메일 판매자에게 전송


10. 택배 송장 확인
    - 구매자에게 판매자가 택배를 발송하면 구매자에게 택배 송장번호 이메일로 전송

11. 판매자 즐겨찾기, 판매글 알림 서비스

## DB 설정

1. 회원
    - 아이디
    - 닉네임
    - 비밀번호
    - 휴대폰
    - 주소
    - 이메일 인증 여부
    - 비밀번호 초기화 코드
    - 판매 수
    - 생성일자
    - 수정일자
    - create table user
      (
      id                  varchar(255) not null primary key, 
      address             varchar(255) null,
      created_at          datetime(6)  null,
      deal_total_count    int          not null,
      email_yn            bit          not null,
      nick_name           varchar(255) null,
      password            varchar(255) null,
      password_reset_code varchar(255) null,
      phone_number        varchar(255) null,
      updated_at          datetime(6)  null
      );


2. 글
    - 아이디
    - 제목
    - 내용
    - 태그
    - 작성자
    - 조회수
    - 좋아요
    - 판매 여부
    - 생성일자
    - 수정일자
    - create table posts
      (
      id         bigint       not null primary key,
      created_at datetime(6)  null,
      is_sailed  bit          not null,
      like_count bigint       null,
      teg        varchar(255) null,
      text       varchar(255) null,
      title      varchar(255) null,
      updated_at datetime(6)  null,
      views      bigint       null,
      writer     varchar(255) null
      );


3. 댓글
    - 내용
    - 작성자
    - 좋아요
    - 생성일자
    - 수정일자
    - create table comments
      (
      id         bigint       not null primary key,
      comments   varchar(255) null,
      created_at datetime(6)  null,
      like_count bigint       null,
      updated_at datetime(6)  null,
      writer     varchar(255) null
      );


4. 주문
    - 주문 번호
    - 택배사
    - 택배 송장
    - 판매자
    - 구매자
    - 수령 여부
    - 생성일자
    - 수정일자
    - create table orders
      (
      order_number     varchar(255) not null primary key,
      buyer            varchar(255) null,
      created_at       datetime(6)  null,
      delivery_company varchar(255) null,
      delivery_number  varchar(255) null,
      is_received      bit          not null,
      seller           varchar(255) null,
      updated_at       datetime(6)  null
      );



