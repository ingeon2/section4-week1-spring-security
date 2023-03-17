package com.codestates.auth;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.Member;
import com.codestates.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class HelloUserDetailsServiceV3 implements UserDetailsService { //UserDetailsService는 남이만든 인터페이스
    //DI
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils helloAuthorityUtils;
    public HelloUserDetailsServiceV3(MemberRepository memberRepository, HelloAuthorityUtils helloAuthorityUtils) {
        this.memberRepository = memberRepository;
        this.helloAuthorityUtils = helloAuthorityUtils;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));


        return new HelloUserDetails(findMember);

    }

    private final class HelloUserDetails extends Member implements UserDetails {
        HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setFullName(member.getFullName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles()); //V3바뀐것
            //HelloUserDetails가 상속하고 있는 Member(extends Member)에 데이터베이스에서 조회한 List<String> roles를 전달
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return helloAuthorityUtils.createAuthorities(this/*멤버*/.getRoles()); //V2와 매개변수가 바뀜 (by 매서드 오버로딩)
            //여기서의 Roles는 List<String> = {유저, 어드민} 이런식
            //Member(extends Member)에 전달한 Role 정보를 authorityUtils.createAuthorities() 메서드의 파라미터로 전달해서 권한 목록(List<GrantedAuthority>)을 생성

        }


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