package com.codestates.member;

import com.codestates.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
//프린시팔 임플리먼츠가 의미하는것 -> 여기 클래스를 스프링 시큐리시상의 User으로 사용하겠다 라는 의미
//생각해보면 스프링 JPA DATA의 @Entity랑 비슷하게 선언해주는느낌?
public class Member extends Auditable implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String fullName;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
           this.status = status;
        }
    }


    //아래 enum
    //⭐ Spring Security에서는 SimpleGrantedAuthority 를 사용해 Role 베이스 형태의 권한을 지정할 때
    //‘ROLE_’ + 권한 명 형태로 지정해 주어야 합니다. 그렇지 않을 경우 적절한 권한 매핑이 이루어지지 않는다는 사실을 기억.
    public enum MemberRole {
        ROLE_USER,
        ROLE_ADMIN
    }



    //상속받은 Principal 구체화해주어야하는 매서드
    @Override
    public String getName() {
        return getEmail();
    }

    //List, Set 같은 컬렉션 타입의 필드는 @ElementCollection 애너테이션을 추가하면
    //User 권한 정보와 관련된 별도의 엔티티 클래스를 생성하지 않아도 간단하게 매핑 처리가 됨. (이말이 핵심)
    //즉, 멤버와 역할을 이어주는 테이블이 생긴다는 뜻(멤버아이디, ROLSE 이런 테이블)
    //얘는 Member의 멤버변수이다. 얘는 Member의 멤버변수이다. 얘는 Member의 멤버변수이다.
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
}
