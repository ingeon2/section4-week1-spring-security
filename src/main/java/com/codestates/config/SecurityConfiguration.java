package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    //아래처럼 HttpSecurity를 파라미터로 가지고, SecurityFilterChain을 리턴하는 형태의 메서드를 정의하면 HTTP 보안 설정을 구성할 수 있음
    //HttpSecurity는 HTTP 요청에 대한 보안 설정을 구성하기 위한 핵심 클래스
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                //frameOptions()는 HTML 태그 중에서 <frame>이나 <iframe>, <object> 태그(표시)는 원래 보안문제로 스프링 시큐리티 불가능.
                //근데 h2 database의 기본콘솔이
                //페이지를 렌더링(보게 되는 대화형 페이지로 바꾸는것)할지의 여부를 결정하는 기능
                //동일 출처로부터 들어오는 request만 페이지 렌더링을 허용

                .and()

                .csrf().disable()
                // CSRF(Cross-Site Request Forgery) 공격에 대한 Spring Security에 대한 설정을 비활성화 (디폴트가 활성화)

                .formLogin()
                //기본적인 인증 방법을 폼 로그인 방식으로 지정

                .loginPage("/auths/login-form")
                //템플릿 프로젝트에서 미리 만들어 둔 커스텀 로그인 페이지를 사용하도록 설정
                // "/auths/login-form"은 AuthController의 loginForm() 핸들러 메서드에 요청을 전송하는 요청 URL

                .loginProcessingUrl("/process_login")
                //로그인 인증 요청을 수행할 요청 URL을 지정, (얘는 login.html 안에 존재)
                //process_login URL로 요청을 전송하면 해당 요청은 Spring Security에서 내부적으로 인증 프로세스를 진행
                //현재 상태에서는 login.html 같은 커스텀 로그인 페이지를 통해 Spring Security가 로그인 인증 처리를 하기 위한 요청 URL을 지정하는 것이라는 사실

                .failureUrl("/auths/login-form?error")
                //로그인 인증에 실패할 경우 어떤 화면으로 리다이렉트 할 것인가를 지정

                .and()
                // Spring Security 보안 설정을 메서드 체인 형태로 구성

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()

                .exceptionHandling().accessDeniedPage("/auths/access-denied")

                //권한이 없는 사용자가 특정 request URI에 접근할 경우 발생하는 403(Forbidden) 에러를 처리하기 위한 페이지를 설정

                .and()

                //authorizeHttpRequests() 메서드는 아래와 같이 람다 표현식을 통해 request URI에 대한 접근 권한을 부여
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/orders/**").hasRole("ADMIN")
                        //ADMIN Role을 부여받은 사용자만 /orders로 시작하는 모든 URL에 접근할 수 있다는 의미
                        //antMatchers는 순서 중요!
                        //더 구체적인 URL 경로부터 접근 권한을 부여한 다음 덜 구체적인 URL 경로에 접근 권한을 부여해야함 (퍼밋올이 먼저나오면 전부 다 허용되어버림.)
                        .antMatchers("/members/my-page").hasRole("USER")
                        .antMatchers("/**").permitAll()
                );

        
//                .authorizeHttpRequests()
//                //클라이언트의 요청이 들어오면 접근 권한을 확인하겠다고 정의
//                .anyRequest()
//                .permitAll();
//                //클라이언트의 모든 요청에 대해 접근을 허용 (★뒤에서 접근 권한 별로 페이지 접근을 제한할 것임.★)
//        이전엔 모든 화면에 접근할 수 있었음.
        
        return http.build(); //디폴트 시큐리티 필터체인 값
    }


//    //여기는 인메모리에서나 사용하는 계정들이라, db방식으로 바꾸면서 userDetailsService 매서드 전체를 주석처리해줄예정
//    //아래는 애플리케이션이 실행된 상태에서 사용자 인증을 위한 계정 정보를 메모리상에 고정된 값으로 설정한 예
//    @Bean
//    public UserDetailsManager userDetailsService() {
//
//        //UserDetails 인터페이스는 인증된 사용자의 핵심 정보를 포함
//        UserDetails user = User.withDefaultPasswordEncoder()
//                //withDefaultPasswordEncoder() 는
//                //디폴트 패스워드 인코더(내부적으로 디폴트 PasswordEncoder 인터페이스를 통해 암호화된다는 사실)를 이용해 사용자 패스워드를 암호화
//                .username("kevin@gmail.com")
//                //username() 메서드는 사용자의 usrname을 설정, 아이디값
//
//                .password("1111")
//                //사용자의 password를 설정
//
//                .roles("USER") //roles() 메서드는 사용자의 Role 즉, 역할을 지정하는 메서드
//                //실제 서비스로 운영되는 대부분의 애플리케이션은 크게 일반 사용자 또는 관리자 역할로 구분되어 관리자가 접속할 수 있는 기능이 별도로 존재,
//                //roles() 메서드가 User의 역할을 지정해주는 기능을 한다고 이해
//
//                .build();
//
//
//
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin@gmail.com")
//                        .password("2222")
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//        //이 방식은 테스트 환경 또는 데모 환경에서만 유용하게 사용할 수 있는 방식임을 참고 (아직 아이디 비번 등록한게 아니라 내가 임의로 생성해준것이라서)
//    }


    //PasswordEncoder를 Bean으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

//SecurityConfiguration 클래스에서 설정해 둔 사용자 계정 정보는 다음과 같습니다.
//email: kevin@gmail.com
//password: 1111
//Role: USER