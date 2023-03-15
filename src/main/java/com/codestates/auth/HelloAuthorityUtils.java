package com.codestates.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelloAuthorityUtils {

    //application.yml에 추가한 프로퍼티를 가져오는 표현식 
    //@Value("${프로퍼티 경로}")의 표현식 형태로 작성하면 application.yml에 정의되어 있는 프로퍼티의 값을 변수에 할당해서 사용할 수 있음.
    @Value("${mail.address.admin}")
    private String adminMailAddress;

    //Spring Security에서 지원하는 AuthorityUtils 클래스를 이용해서 관리자용 권한 목록을 List<GrantedAuthority> 객체로 미리 생성
    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    // Spring Security에서 지원하는 AuthorityUtils 클래스를 이용해서 일반 사용 권한 목록을 List<GrantedAuthority> 객체로 미리 생성
    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");


    //파라미터로 전달받은 이메일 주소가 application.yml 파일에서 가져온 관리자용 이메일 주소와 동일하다면
    //관리자용 권한인 List<GrantedAuthority> ADMIN_ROLES를 리턴
    public List<GrantedAuthority> createAuthorities(String email) {
        if(email.equals(adminMailAddress)) {
            return ADMIN_ROLES;
        }
        return USER_ROLES;
    }
    //원래 위와 같이 관리자 권한 부여하는게 아닌데, 학습 목적이라 간단한 로직으로 관리자 권한 부여.
}
