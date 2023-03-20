package com.codestates.member;

public interface MemberService {
    Member createMember(Member member);
    //애플리케이션은 회원 가입 폼에서 전달받은 정보를 이용해 새로운 사용자를 추가하는 기능만 있으면 되므로
    //createMember() 하나만 구현하는 구현체가 있으면 됨.

    Member findMember(String email);
}
