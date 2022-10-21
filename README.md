# 📕 도서데이터 검색엔진 구현

## 프로젝트의 목표😎 ##
1. **만족스러운 검색 속도를 가져야함**
2. **검색어에 부합한 검색결과들을 사용자에게 보여줘야 함**
3. **단순한 검색기능만 제공하는 것이 아니라 다른 서비스도 구현을 해야함**
   
## 사용 아키텍쳐
![아키텍처](https://user-images.githubusercontent.com/100353794/197140964-7495467e-7a64-4bcd-a18a-f7c8ee5187db.PNG)

**파일 구조**  
```
main
└─com
  └─example
          └─elasticsearchtest
              ├─Auth
              ├─config
              ├─controller
              ├─domain
              ├─dto
              │  ├─Request
              │  └─Response
              ├─Errorhandler
              ├─jwt
              ├─repository
              └─service
```
**주요 화면**  
![1](https://user-images.githubusercontent.com/100353794/197157337-615ccb75-a91e-4aca-9bc6-425b49f0cf5f.png)

![2](https://user-images.githubusercontent.com/100353794/197157353-3266f1ae-46bb-4714-b260-0ac96626bc37.png)

![3](https://user-images.githubusercontent.com/100353794/197157369-26e189db-4bfc-4f41-8db6-d1265a5e4dc5.png)
## API 리스트
|기능|메소드|URL|
|------|---|-----------|
|로그인 페이지 가져오기|GET|/page/login|
|아이디 중복체크|GET|/register/exists|
|회원가입|POST|/register|
|로그인|POST|/login|
|로그아웃|GET|/login|
|사용자아이디 가져오기|GET|/getnickname|
|메인페이지 가져오기|GET|/|
|자동완성 제목 가져오기|GET|/autocomplete_book|
|도서 검색하기|GET|/search?keyword= &type= &page= |
|도서 상세검색하기|GET|/fullsearch?bookname=&authors=@&publisher=@&firstPublication=&endPublication=&genre=&library=@&page=|
|상세페이지 가져오기|GET|/search_isbn/{isbn13}|
|댓글 작성|POST|/comment|
|댓글 삭제|POST|/delete_comment|
|댓글 수정|POST|/modify_comment|
|마이페이지 가져오기|GET|/my-page/{id}|
<br>

## ERD   

## 트러블슈팅



