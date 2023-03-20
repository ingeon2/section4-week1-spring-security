package com.codestates.config;

import com.codestates.auth.utils.HelloAuthorityUtils;
import com.codestates.member.DBMemberService;
import com.codestates.member.MemberRepository;
import com.codestates.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JavaConfiguration {
    //여기서 뭘 갈아끼냐에 따라 인메모리, db 나뉘게됨 (멤버 패키지에 서비스 두가지 존재)
    //JavaConfiguration 클래스에서는 MemberService 인터페이스의 구현 클래스인
    //InMemoryMemberService, 혹은 DBMemberService를 Spring Bean으로 등록


    //빈등록
    //InMemoryMemberService 클래스는 데이터베이스 연동 없이 메모리에 Spring Security의 User를 등록해야 하므로 UserDetailsManager 객체가 필요.
    // (원래라면 User등록시 db에 저장 후 db에서 꺼내와서 사용하므로 유저디테일스메니저 말고 다른 객체로 사용.)
    //또한, User 등록 시, 패스워드를 암호화한 후에 등록해야 하므로 Spring Security에서 제공하는 PasswordEncoder 객체가 필요

//    @Bean
//    public MemberService InMemoryMemberService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
//        return new InMemoryMemberService(userDetailsManager, passwordEncoder);
//    }
//    주석 이유는 DB로 바꿔줄거라서~ 주석 이유는 DB로 바꿔줄거라서~ 주석 이유는 DB로 바꿔줄거라서~ 주석 이유는 DB로 바꿔줄거라서~

    @Bean
    public MemberService dbMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, HelloAuthorityUtils helloAuthorityUtils) {
        return new DBMemberService(memberRepository, passwordEncoder, helloAuthorityUtils);
    }
    //DBMemberService는 내부에서 데이터를 데이터베이스에 저장하고, 패스워드를 암호화해야 하므로
    //위와 같이 MemberRepository와 PasswordEncoder 객체를 DI
    //V3로 바뀌며 헬로오더리티유틸스도 생성자 추가
}
