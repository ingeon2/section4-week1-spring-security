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
import java.util.Optional;

@Component
public class HelloUserDetailsServiceV2 implements UserDetailsService { //UserDetailsService는 남이만든 인터페이스
    //DI
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils helloAuthorityUtils;
    public HelloUserDetailsServiceV2(MemberRepository memberRepository, HelloAuthorityUtils helloAuthorityUtils) {
        this.memberRepository = memberRepository;
        this.helloAuthorityUtils = helloAuthorityUtils;
    }


    //UserDetailsService 인터페이스가 가진 loadUserByUsername 추상매서드 구현한것
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        //V1에 비해 개선된부분, Custom UserDetails 클래스의 생성자로 findMember를 전달하면서 코드가 조금 더 깔끔 (위에잇던 List도 사라짐)
        //이전엔 UserDetails 의 구현체인 User으로 리턴했어서 생성자가 달라. (유저는 생성자 안에 권한도 갈겨줬어야했음)
        return new HelloUserDetails(findMember);

    }
    //HelloUserDetails 클래스 추가
    //(데이터베이스에서 조회한 회원 정보를 Spring Security의 User 정보로 변환하는 과정과 User의 권한 정보를 생성하는 과정을 캡슐화)
    private final class HelloUserDetails extends Member implements UserDetails {
        HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setFullName(member.getFullName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return helloAuthorityUtils.createAuthorities(this.getEmail());
            // HelloAuthorityUtils의 createAuthorities() 메서드를 이용해 User의 권한 정보(유저인지 어드민인지)를 생성
        }


        //Spring Security에서 인식할 수 있는 username을 Member 클래스의 email 주소로 채우고 있음.
        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }


}
