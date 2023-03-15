package com.codestates.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auths")
public class AuthController {
    @GetMapping("/login-form")
    public String loginForm() {
        return "login";
    }


    @PostMapping("/login")
    public String login() {
        System.out.println("Login successfully!");
        return "home";
    }

//    //이거 SecurityConfiguration 클래스의 .exceptionHandling().accessDeniedPage("/auths/access-denied")
//    //로직 안되길래 추가해준 매서드
//    @GetMapping("/access-denied")
//    public String accessDenied() {
//        return "access-denied";
//    }
}
