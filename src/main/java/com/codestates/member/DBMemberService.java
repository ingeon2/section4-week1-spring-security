package com.codestates.member;

import com.codestates.auth.HelloAuthorityUtils;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class DBMemberService implements MemberService {

    //DI
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HelloAuthorityUtils helloAuthorityUtils; //Role 테이블 저장 로직 위해 불렀다!

    public DBMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, HelloAuthorityUtils helloAuthorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.helloAuthorityUtils = helloAuthorityUtils;
    }



    @Override
    public Member createMember(Member member) {
        verifyExistEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword());

        member.setPassword(encryptedPassword);
        //암호화된 패스워드를 password 필드에 다시 할당
        //패스워드 같은 민감한(sensitive) 정보는 반드시 암호화되어 저장
        
        //Role을 DB에 저장하는 로직
        List<String> roles = helloAuthorityUtils.createRoles(member.getEmail()); //얘도 HelloAuthorityUtils 클래스에서 V3를위해만듦
        member.setRoles(roles);

        Member savedMember = memberRepository.save(member);

        System.out.println("# Create Member in DB");
        return savedMember;
    }

    //위에서 사용할 매서드
    private void verifyExistEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if(member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
}
