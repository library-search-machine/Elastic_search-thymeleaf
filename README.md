
## 프로젝트의 목표😎
1. **만족스러운 검색 속도를 가져야함**<br>
   (많이 사용하는 검색엔진인 ElasticSearch를 Spring과 함께 사용)
   
2. **검색어에 부합한 검색결과들을 사용자에게 보여줘야 함**<br>
   (검색종류에 따라 다른 쿼리문을 통해 만족스러운 검색결과를 가지게함 -> ES에서 제공하는 형태소 분석기 nori,spring에서 제공하는 NativeSearchQuery 적용)
   
3. **단순한 검색기능만 제공하는 것이 아니라 다른 서비스도 구현을 해야함**<br>
   (최근 조회한 도서를 기반으로 한 추천 도서목록 서비스, 코멘트 작성기능 구현)
   
## ⚒Tech Stack
 <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">&nbsp;<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/mongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/redis-A41E11?style=for-the-badge&logo=redis&logoColor=white">


## 아키🛠
![아키텍처](https://user-images.githubusercontent.com/100353794/197140964-7495467e-7a64-4bcd-a18a-f7c8ee5187db.PNG)

## 파일 구조💾
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
## 주요 화면🖥
![1](https://user-images.githubusercontent.com/100353794/197157337-615ccb75-a91e-4aca-9bc6-425b49f0cf5f.png)
![2](https://user-images.githubusercontent.com/100353794/197157353-3266f1ae-46bb-4714-b260-0ac96626bc37.png)
![3](https://user-images.githubusercontent.com/100353794/197157369-26e189db-4bfc-4f41-8db6-d1265a5e4dc5.png)


## api명세서📃
|기능|메소드|URL|
|------|-------|---------|
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
## 📖기능 구현 List

1. **회원가입**
    - **Id 중복 검사**
    - **Id 유효성 검사**
    - **PW 및 PW Confirm 유효성 검사**
    
2. **로그인**
    - **id, PW 입력시 공백 유효성 검사**
    - **Id, PW 일치 검사**
    - **Access Token과 Refresh Token를 localStorage에 저장**
    
3. **메인 페이지**
    - **사용자가 조회한 책이 있다면 관련된 추천 도서들을 보여줌 <br>
        (Cookie에 저장된 도서의 정보를 활용)**
    - **검색기능 구현 한가지 항목으로 검색을 하거나 여러 조건으로 검색을 할 수있는 상세검색기능 제공**
    - **검색어 자동완성 기능 제공** 
4. **도서 상세페이지**
   - **상세 페이지에 들어가면 해당 도서의 isbn정보가 cookie에 저장**
   - **해당 도서의 정보를 제공**
   - **해당 도서를 가지고 있는 도서관의 목록들을 확인할 수 있음**
   - **도서에 대해 comment를 작성할 수 있음 (로그인 정보가 없으면 로그인 창으로 이동)**
   -    
5. **마이 페이지**
    - **로그인 정보가 없으면 로그인창으로 이동**
    - **자신이 조회했던 책들을 조회할 수 있음 책들을 클릭하면 해당 도서 상세페이지로 이동**  
    - **자신이 작성했던 댓글들을 확인할 수 있고 삭제와 수정이 가능함.**
## ERD⚙
![erd](https://user-images.githubusercontent.com/100353794/197386822-24305e2a-6dcd-4e20-983f-869c6587ce1a.PNG)


## MVP기능🎇 및 트러블슈팅🧨
   - **프로젝트를 진행하면서 발생했던 이슈와 트러블슈팅은 우리 팀의 notion페이지에 작성하였으니 하단 링크를 방문하면 확인할 수 있다~😊<br>
     https://nonchalant-sturgeon-21a.notion.site/9-73a7e47912c14b85b8be59a82caf0f59**



