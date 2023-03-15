섹션 3에서 여러분들이 만들어 본 샘플 애플리케이션은 학습용으로 꽤 잘 만들어진 애플리케이션이지만  
보안이라는 가장 중요한 요소가 전혀 고려되어 있지 않은 상태  

✅ 로그인 기능(인증, Authentication)이 없음  
일반적으로 온라인상에서 어떤 서비스를 이용할 때 ID/패스워드 같은 인증 정보를 사용하여 자격을 증명하는 로그인 기능이 없음.

✅ API에 대한 권한 부여(인가, Authorization) 기능이 없음  
손님이 매장에서 판매하는 커피 정보를 마음대로 등록할 수 없어야 하는데 여러분이 만든 샘플 애플리케이션에서는 손님이 마음대로 커피 정보를 등록할 수 있음.  
  
→→→→→→→→→→→→ 바로 Spring Security라는 보안 프레임워크를 이용해서 문제 해결해야함.  

Spring Security란?
Spring Security는 Spring MVC 기반 애플리케이션의 인증(Authentication)과 인가(Authorization or 권한 부여) 기능을 지원하는 보안 프레임워크로써,  
Spring MVC 기반 애플리케이션에 보안을 적용하기 위한 사실상의 표준  
  
용어 정리  
Principal(주체) - 계정 정보  
Authentication(인증) - 본인이 맞음을 증명하는 절차를 의미(ex - 로그인)  
Authorization(인가 또는 권한 부여) - 특정 리소스에 접근할 수 있게 허가하는 과정, 인가는 반드시 인증 과정 이후 수행되어야 하며 권한은 일반적으로 역할(Role) 형태로 부여  
Access Control(접근 제어) - 사용자가 애플리케이션의 리소스에 접근하는 행위를 제어하는 것을 의미  

핵심 포인트
Spring Security는 Spring MVC 기반 애플리케이션의 인증(Authentication)과 인가(Authorization or 권한 부여) 기능을 지원하는 보안 프레임워크로써,  
Spring MVC 기반 애플리케이션에 보안을 적용하기 위한 사실상의 표준이다.  
Principal(주체)은 일반적으로 인증 프로세스가 성공적으로 수행된 사용자의 계정 정보를 의미한다.  
Authentication(인증)은 애플리케이션을 사용하는 사용자가 본인이 맞음을 증명하는 절차를 의미한다.  
Authorization(인가 또는 권한 부여)은 Authentication이 정상적으로 수행된 사용자에게 하나 이상의 권한(authority)을 부여하여  
특정 애플리케이션의 특정 리소스에 접근할 수 있게 허가하는 과정을 의미한다.  
Credential(신원 증명 정보)은 Authentication을 정상적으로 수행하기 위해서는 사용자를 식별하기 위한 정보를 의미한다.  
Access Control(접근 제어)은 사용자가 애플리케이션의 리소스에 접근하는 행위를 제어하는 것을 의미한다.  

✅ Spring Security를 사용해야 하는 이유  
결론부터 이야기하자면 애플리케이션의 보안을 강화하기 위한 솔루션으로 Spring Security만 한 다른 프레임워크가 존재하지 않기 때문  
때로는 Spring Security의 기본 옵션으로 만족시킬 수 없는 특정 보안 요구 사항을 만족시켜야 할 경우가 있는데,  
이 경우 Spring Security를 사용하면 특정 보안 요구 사항을 만족시키기 위한 코드의 커스터마이징이 용이하고 유연한 확장이 가능.  
  
핵심 포인트  
보안 기능을 밑바닥부터 직접 구현하는 것보다 잘 검증되어 신뢰할 만한 Spring Security를 사용하는 것이 더 나은 선택이다.  
Spring Security는 특정 보안 요구 사항을 만족하기 위한 커스터마이징이 용이하고, 유연한 확장이 가능  
  
여기 어플리케이션의 리소스 패키지엔 html문서들 있어서 실행시 각각에 맞게 띄워준다!  

핵심 포인트
보안 기능을 밑바닥부터 직접 구현하는 것보다 잘 검증되어 신뢰할 만한 Spring Security를 사용하는 것이 더 나은 선택이다.
  
  
  
여기서부터 코드에 손대기 시작  
Spring Security 의존 라이브러리 build.gradle에 추가해주기.  
그대로 추가하고 어플실행하면 Spring Security 디폴트값으로 로그인 페이지가 나오는데, 그때마다 로그인 할 수 없으니 바꿔주어야함.  
(애플리케이션을 매번 실행할 때마다 패스워드가 바뀌는 것도 문제,  
Spring Security에서 제공하는 디폴트 인증 정보만으로는 회원 각자의 인증 정보로 로그인을 하는 것 역시 사실상 불가능)  
  
그러면 어케하지..?  
Spring Security의 Configuration을 통해 우리의 입맛에 맞는 인증 방식을 설정.(config 패키지의 SecurityConfiguration)  
SecurityConfiguration 클래스의  
userDetailsService 매서드는 데모 방식의 Spring Security 디폴트 로그인 창 매서드,  
filterChain 매서드는 디폴트 로그인 창 사용하지 않고 우리의 resource 패키지에 있는 로그인.html 사용하도록 userDetailsService 기반으로 설정 추가함.  
  
SecurityConfiguration 클래스에서 설정해 둔 사용자 계정 정보는 다음과 같습니다.  
email: kevin@gmail.com  
password: 1111  
Role: USER  
  
이제 filterChain 매서드에서 사용자의 Role 별로 request URI에 접근 권한이 부여되도록 수정.  
(이전엔 .authorizeHttpRequests().anyRequest().permitAll(); 설정을 통해 로그인 인증에 성공할 경우, 모든 화면에 접근할 수 있도록 했음.)  
현재는 ADMIN 인지 USER 인지에 따라 권한 다르게 로직 짬.  

현재 화면에서는 사용자가 로그인한 후에 어떤 사용자가 로그인했는지 알 수 없습니다.  
또한 로그인 한 사용자가 로그 아웃을 할 수 있는 기능도 없습니다.  
그리고 마이 페이지 링크 역시 로그인 한 사용자에게만 보이는 것이 좋을 것 같습니다.  
이 세 가지 기능을 처음 화면인 header.html에 추가.  
기능 추가 후 로그아웃 로직 구현을 SecurityConfiguration에 추가해줌.  

핵심 포인트  
Spring Security의 기본 구조와 기본적인 동작 방식을 이해하기 가장 좋은 인증 방식은 폼 로그인 인증 방식이다.  
Spring Security를 이용한 보안 설정은 HttpSecurity를 파라미터로 가지고, SecurityFilterChain 을 리턴하는 Bean을 생성하면 된다.  
HttpSecurity를 통해 Spring Security에서 지원하는 보안 설정을 구성할 수 있다.  
로컬 환경에서 Spring Security를 테스트하기 위해서는 CSRF 설정을 비활성화해야 한다.  
InMemoryUserDetailsManager 를 이용해 데이터베이스 연동없이 테스트 목적의 InMemory User를 생성할 수 있다.  

이전 챕터에서 InMemory User를 이용해 애플리케이션 실행 시, 메모리에 두 개의 사용자 정보를 미리 등록(kevin@gmail.com, admin@gmail.com)한 후  
사용자의 권한 별로 request URL의 접근이 제한되는지 여부 등을 살펴보았음.  
  
  
  
기본 구조 2 시작.  
현재 상태로는 회원 가입이 에러없이 진행 되는 것 같지만 가입한 회원 정보로 로그인해보면 로그인 인증에 실패.  
(애플리케이션에서 회원 가입 요청으로 전달받은 회원 정보를 Spring Security가 알 수 있는 어떠한 처리도 하지 않았기 때문.)  
회원 가입 기능을 제대로 동작하도록 코드를 수정.(SecurityConfiguration, JavaConfiguration, InMemoryMemberService 클래스에서.)  

회원 가입 폼을 통해 InMemory User를 등록하기 위한 작업 순서는 다음과 같습니다.  
PasswordEncoder Bean 등록(SecurityConfiguration 클래스에)  
(회원 가입 폼에서 전달받은 패스워드는 InMemory User로 등록하기 전에 암호화 되어야 함, 패스워드 암호화 기능을 제공하는 컴포넌트)  
  
MemberService Bean 등록을 위한 JavaConfiguration 구성  

InMemoryMemberService 클래스 구현, 구현한 정보에 따라(DI, 매서드) JavaConfigration 변경.  
(createMember 매서드로 회원가입 시켜야하니.)  
  
  
여기까지 마쳤는데, 여기까진 전부 InMemoryMemberService를 사용해서 메모리 안에 저장했음.  
이 방법은 어플리케이션 중지시키면 사라지기에 데모환경, 테스트 환경에서나 사용하는 방법임.  
지금까지와는 다르게, 이제 테이블에 저장하고 그 정보를 이용해 인증 프로세스를 이용할것임.  
Custom UserDetailsService를 이용해 User의 로그인 인증을 어떻게 처리하는지 지금부터 살펴보도록 하겠음.  

지금까지는 User로 사용했고, 데이터 저장 장소 바꾸면서  
애플리케이션에서는 Member 엔티티 클래스가 로그인 인증 정보를 포함할 텐데 이 Member 엔티티가 Spring Security의 User 정보를 포함한다고 보면 됨.  
SecurityConfiguration 클래스에선 기존 userDetailsService 삭제(인메모리에서 사용하니까, 앞으로는 repository로 db에 저장할거거든)  
JavaConfiguration 클래스에선 Config역할에 충실하며 갈아끼움(인메모리 -> DB)  
그다음 DBMemberService 클래스에 비즈니스 로직들(리파지토리 save 가져와서 create로) 완성.  
그다음 Spring Security에서 제공하는 컴포넌트 중 하나인 UserDetailsService 를 구현하는 HelloUserDetailsServiceV1 클래스 생성.  
(InMemory User를 등록하는 데 사용했던 InMemoryUserDetailsManager 는 UserDetailsManager 인터페이스의 구현체이고,  
UserDetailsManager 는 UserDetailsService 를 상속하는 확장 인터페이스라는 점 기억.)  
HelloUserDetailsServiceV1 클래스를 잘 생성하기 위해, HelloAuthorityUtils 클래스 또한 생성.  
엔티티를 db에 저장하는것과 db에서 꺼내와서 스프링 시큐리티로 사용하는 로직은 다르다.  
즉, 저장하는것은 리파지토리에서부터 파생되어서 저장되고, 사용하는것은 HelloUserDetailsServiceV1 클래스가 구현한 추상매서드에서 실행된다.  
여기까지 이해 가는가? 클래스들 열어보며 따라와라 어려워도.  
