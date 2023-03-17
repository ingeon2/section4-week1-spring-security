package com.codestates.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
//Spring Security는 아래와 같이 AuthenticationProvider를 구현한 구현 클래스가 Spring Bean으로 등록되어 있다면
//해당 AuthenticationProvider를 이용해서 인증을 진행
public class HelloUserAuthenticationProvider implements AuthenticationProvider {

    //DI
    private final HelloUserDetailsServiceV3 userDetailsService;
    private final PasswordEncoder passwordEncoder;
    public HelloUserAuthenticationProvider(HelloUserDetailsServiceV3 userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }



    //우리가 직접 작성한 인증 처리 로직을 이용해 사용자의 인증 여부를 결정
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        //authentication을 캐스팅하여 UsernamePasswordAuthenticationToken을 얻음.

        String username = authToken.getName();
        Optional.ofNullable(username).orElseThrow(() -> new UsernameNotFoundException("Invalid User name or User Password"));
        //UsernamePasswordAuthenticationToken 객체에서 해당 사용자의 Username을 얻은 후, 존재하는지 체크


        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //Username이 존재한다면 userDetailsService를 이용해 데이터베이스에서 해당 사용자를 조회
            //내가 loadUserByUsername 이 매서드 어따쓰지? 생각했는데 드디어 쓰네
            
            String password = userDetails.getPassword();
            verifyCredentials(authToken.getCredentials(), password);
            //로그인 정보에 포함된 패스워드(authToken.getCredentials())와 데이터베이스에 저장된 사용자의 패스워드 정보가 일치하는지를
            //내가 만든 매서드로 검증

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            //검증 과정을 통과했다면 로그인 인증에 성공한 사용자이므로 해당 사용자의 권한을 생성
            //여기 getAuthorities 매서드 HelloAuthorityUtils 에서 만든것.

            return UsernamePasswordAuthenticationToken.authenticated(username, password, authorities);
            //인증된 사용자의 인증 정보를 리턴값으로 전달
        }
        catch (Exception ex) {
            //회원가입 안해서 아직 존재 안한다면 이쪽 로직으로 빠지게 됨
            throw new UsernameNotFoundException(ex.getMessage());
        }
        //Custom AuthenticationProvider에서 AuthenticationException이 아닌 Exception이 발생할 경우에는
        //꼭 AuthenticationException을 rethrow 하도록 코드를 구성해야 한다는 사실을 기억
    }




    //우리가 구현하는 Custom AuthenticationProvider(HelloUserAuthenticationProvider)가 Username/Password 방식의 인증을 지원한다는 것을 Spring Security에 알려주는 역할
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
    
    
    //인증 절차를 위해 내가 만든 매서드
    private void verifyCredentials(Object credentials, String password) {
        if(!passwordEncoder.matches((String) credentials, password)) {
            throw new BadCredentialsException("Invalid User name or User Password");
        }
    }
}
