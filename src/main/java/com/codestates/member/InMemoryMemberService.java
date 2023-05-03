package com.codestates.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryMemberService implements MemberService {


    //인메모리라서(db없어서) 아래의 객체 InMemoryMemberService 생성에 필요함,
    //UserDetailsManager는 Spring Security의 User를 관리하는 관리자 역할
    private final UserDetailsManager userDetailsManager;
    //얘는 받아오는 패스워드 암호화에 필요함.
    private final PasswordEncoder passwordEncoder;

    //DI
    public InMemoryMemberService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }



    //막간 상식. @Override는 꼭 필요할까? 아니다. 일종의 안전장치인 셈.
    //여기서의 MemberService는 내가 만든 인터페이스이기에 상관없지만,
    //다른 사람이 만든 라이브러리와 같은 것들에서 매서드 이름이 혹시 바뀐다면,
    //컴파일시 @Override 없이는 그냥 다른 매서드라고 인식할것
    
    //그리고 여기서의 createMember 매서드는 스프링 시큐리티에서 유저를 등록하는 매서드임,
    // 즉 스프링 시큐리티의 유저등록은 인메모리에서 멤버생성과 마찬가지임. (이해해보기)
    @Override
    public Member createMember(Member member) {
        List<GrantedAuthority> authorities = createAuthorities(Member.MemberRole.ROLE_USER.name());
        //User의 권한 목록을 List<GrantedAuthority>로 생성
        //Member 클래스에는 MemberRole 이라는 enum이 정의되어 있고, ROLE_USER와 ROLE_ADMIN이라는 enum 타입이 정의되어 있음.

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        //Member의 패스워드를 받아서 PasswordEncoder 를 이용해 등록할 User의 패스워드를 암호화
        //패스워드를 암호화하지 않고 User를 등록한다면 User 등록은 되지만 로그인 인증 시, 에러를 만나게 되므로 User의 패스워드는 반드시 암호화함.

        UserDetails userDetails = new User(member.getEmail(), encryptedPassword, authorities);
        //Spring Security User로 등록하기 위해 UserDetails 를 생성

        userDetailsManager.createUser(userDetails);
        //UserDetailsManager의 createUser() 메서드를 이용해서 User를 등록

        return member;
    }

    @Override
    public Member findMember(String email) {
        return null;
    }


    //Java의 Stream API를 이용해 생성자 파라미터로 해당 User의 Role을 전달하면서 SimpleGrantedAuthority 객체를 생성한 후,
    //List<SimpleGrantedAuthority> 형태로 리턴
    private List<GrantedAuthority> createAuthorities(String... roles) { //...은 가변인자로 말 그대로 여러 개의 매개변수를 받을 수 있다는 말
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role))
                //SimpleGrantedAuthority 객체 생성할때, 매개변수 String 은 ROLE_ 이렇게시작되어야 함(Member 클래스 가서 enum 보렴)
                .collect(Collectors.toList());
    }
}
