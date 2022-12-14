package com.example.elasticsearchtest.controller;


import com.example.elasticsearchtest.Errorhandler.BusinessException;
import com.example.elasticsearchtest.domain.Member;
import com.example.elasticsearchtest.jwt.TokenProvider;
import com.example.elasticsearchtest.repository.MemberRepository;
import com.example.elasticsearchtest.response.BookReviewResponse;
import com.example.elasticsearchtest.response.MyPageResponseDto;
import com.example.elasticsearchtest.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.elasticsearchtest.Errorhandler.ErrorCode.JWT_NOT_PERMIT;

@RequiredArgsConstructor
@Controller
//최근 내가 본 책들과 댓글 리스트를 리턴을 함...
//어떻게 해야하는 것인가.. 지금 내가 본거는 현재 쿠키에 있는 값을 뿌려주면 되고 리스트는 지금 나의 닉네임으로 작성된 댓글들을 불러오면 되는거임..?
public class MyPageController {
    private final MyPageService myPageService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    @GetMapping("/my-page/{id}")
    public String my_page(Model model, HttpServletRequest request, HttpServletResponse response,@PathVariable("id") String id) {
        //토큰 검증과정
        Member member = memberRepository.findByNickName(id).get();
        Cookie[] cookies = request.getCookies();//전체 쿠키를 받는 애 여기서 isbn만 뽑아와야할듯합니다.//현재 쿠키들에는 isbn이 저장이 되어있음 어떻게 불러옴..?
        List<String> isbnList = new ArrayList<>();
        List<MyPageResponseDto> bookList = new ArrayList<>();
        if (cookies != null) {//이상한 쿠키가 있으면 안됨... 그러면 안됨...ㅠㅠ
            isbnList = new ArrayList<>();
            for (Cookie cookie : cookies) {
                isbnList.add(cookie.getValue());
            }//이제 여기에는  isbn이 저장되어 있으니깐.그것을 저장을 하는 거임. 책제목이랑 그 뭐지 지은이만 나오는걸로...합시다.. 이건 어떻게..?
            bookList=myPageService.test(isbnList);
            Collections.reverse(bookList);//최근 순으로 정렬하기 위해서 collections reverse 함
        }
        List<BookReviewResponse> commentList = myPageService.findByNickname(member.getNickName());
        model.addAttribute("commentList", commentList);//해당 그 게시물이 없으면 뭐 알아서 해주겠죠..?
        model.addAttribute("bookList", bookList);//해당 그 게시물이 없으면 뭐 알아서 해주겠죠..?
        return "my_page";
    }


    public Member TokenValidation(HttpServletRequest request){
        //토큰 검증과정
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            throw new BusinessException("잘못된 JWT 토큰입니다", JWT_NOT_PERMIT);
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
