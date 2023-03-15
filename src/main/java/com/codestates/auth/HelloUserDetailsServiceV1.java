package com.codestates.auth;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.Member;
import com.codestates.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class HelloUserDetailsServiceV1 implements UserDetailsService { //UserDetailsService는 남이만든 인터페이스
    //DI
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils helloAuthorityUtils;
    public HelloUserDetailsServiceV1(MemberRepository memberRepository, HelloAuthorityUtils helloAuthorityUtils) {
        this.memberRepository = memberRepository;
        this.helloAuthorityUtils = helloAuthorityUtils;
    }

    
    //HelloUserDetailsServiceV1 클래스는 UserDetailsService 인터페이스를 implements 하는 구현 클래스라서
    //UserDetailsService 인터페이스가 가진 loadUserByUsername 추상매서드 구현한것
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        //매개변수 username으로 멤버 찾아내고 있는지 확인한 후

        //HelloAuthorityUtils 를 이용해 데이터베이스에서 조회한 회원의 이메일 정보를 이용해
        //Role 기반의 권한 정보(GrantedAuthority) 컬렉션을 생성
        Collection<? extends GrantedAuthority> authorities = helloAuthorityUtils.createAuthorities(findMember.getEmail());

        //스프링의 User 객체는 권한도 넣어줘야함, 이미 멤버 생성할때 passwordencoder로 암호화되어서 set 된 후라서 이후로는 암호화 안해도 댐.
        return new User(findMember.getEmail(), findMember.getPassword(), authorities);

    }


}
