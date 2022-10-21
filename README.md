# Elastic_search-thymeleaf
파일구조   
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

> 도서관 검색 사이트
> 프로젝트 소개 README.md, issue, PR, Project board

[![author-ohahohah](https://img.shields.io/badge/author-siyoungoh-blue.svg)](https://github.com/siyoungoh/)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/siyoungoh/proj-template/graphs/commit-activity) 
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](http://makeapullrequest.com)  
***유용하게 쓰셨다면 상단 star [![GitHub stars](https://img.shields.io/github/stars/siyoungoh/proj-template.svg?style=social)](https://github.com/siyoungoh/proj-template) 를 눌러주세요!***

**주요 화면**  
![GIF]()

## 프로젝트 MVP기능😎
- template 제공 - 상단 초록색 버튼 [`Use this template`](https://github.com/siyoungoh/proj-template/generate)을 사용해 템플릿으로 사용해보세요.
- 프로젝트 파악을 위한 기본 설명 [README.md](https://github.com/siyoungoh/proj-template/blob/master/README.md)
- [issue](https://github.com/siyoungoh/proj-template/issues/1)
  - 작업 또는 버그 추적, 아이디어, 피드백(comment, 멘션 `@사용자이름` 등)
  - label, assignees 으로 관련자 할당, 이슈 성격 
  - 프로젝트 차원에서 관리 project board, milestone
  - issue 설정파일: issue 성격에 따라 다른 [Issue template](https://github.com/siyoungoh/proj-template/issues/new/choose)을 사용해보세요. / [설정파일](https://github.com/ohahohah/github-tutorial/tree/master/.github/ISSUE_TEMPLATE)
- Branch 전략(github flow, gitlab flow, git flow 등)은 하단 [참고자료]()를 참고
- [PR]()
  - [PR 설정파일]()
- issue, PR 카드 포함한 프로젝트 관리 [project board](https://github.com/siyoungoh/proj-template/projects/1)
- [PR없이 할 수 있는 코드 리뷰 방법 - Commit Comment](https://github.com/siyoungoh/proj-template/issues/4)
  - PR을 할 경우, 되도록 PR 내 코드리뷰 기능을 활용 권장. 사용하지 않을 경우, 작업내용(commit)에 댓글 달기. PR없이도 Code Review 용도로, 질문하는 용도로 사용할 수도 있음. 
- [gitignore 파일](https://github.com/siyoungoh/proj-template/blob/master/.gitignore)
  - git이 추적할 필요가 없는 설정 파일 등. 예: 내 로컬 개발환경 외에 필요없는 불필요한 설정파일(venv, IDE설정), 공개되면 안되는 보안 파일
- [샘플 프로젝트 보드](https://github.com/ohahohah/github-tutorial/projects/1) 를 참고해 내 프로젝트를 관리하세요.  

> *프로젝트 포트폴리오로 사용할 경우 아래 추가하면 상대가 파악하기 쉽습니다.*
## 아키텍쳐
![아키텍처](https://user-images.githubusercontent.com/100353794/197140964-7495467e-7a64-4bcd-a18a-f7c8ee5187db.PNG)


## API 리스트
|기능|메소드|URL|
|------|---|---|
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


## Contribution
- Contribution을 환영합니다.
- github issue 로 먼저 Feature, Bug fix 제안을 선호합니다만 바로 PR만 해도 괜찮습니다. (참고. [첫 기여하기](https://github.com/firstcontributions/first-contributions/blob/master/translations/README.ko.md#%EB%B3%80%EA%B2%BD%EC%82%AC%ED%95%AD%EC%9D%84-%EA%B9%83%ED%97%88%EB%B8%8C%EC%97%90-%ED%91%B8%EC%8B%9C%ED%95%98%EA%B8%B0)) 

## Reference
- [github 튜토리얼]()
- [Markdown tutorial](https://www.markdowntutorial.com/kr/) / [Markdown Cheat-sheet](https://www.markdownguide.org/cheat-sheet/)

## Links
- Project homepage: https://github.com/siyoungoh/proj-template/
- Repository: https://github.com/siyoungoh/proj-template/
- Issue tracker: https://github.com/siyoungoh/proj-template/issues
  - 보안 취약점 등의 민감한 이슈인 경우 [이메일 주소](mailto:이메일@주소)로 연락주십시오. 
- 관련 프로젝트
  - Git README 템플릿 : https://github.com/템플릿
  
## License
SiyoungOh – [이메일주소](mailto:이메일주소)  

MIT license를 준수합니다. [LICENSE](LICENSE)에서 자세한 정보를 확인할 수 있습니다.  
 https://github.com/yourname/yourproject/wiki
