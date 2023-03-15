package com.codestates.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    //아래 레지스터 겟매핑 포스트매핑 두개. 하나는 안내역할 하나는 등록역할
    @GetMapping("/register")
    public String registerMemberForm() {
        return "member-register";
    }

    @PostMapping("/register")
    public String registerMember(@Valid MemberDto.Post requestBody) {
        Member member = mapper.memberPostToMember(requestBody);
        memberService.createMember(member); 
        //위가 우리가 InMemoryMemberService 에서 손봐준 매서드.
        //우리가 입력한 값으로(리퀘스트바디) 스프링 시큐리티에서 인메모리에 User 등록해줌.

        System.out.println("Member Registration Successfully");
        return "login";
    }

    @GetMapping("/my-page")
    public String myPage() {
        return "my-page";
    }
}
