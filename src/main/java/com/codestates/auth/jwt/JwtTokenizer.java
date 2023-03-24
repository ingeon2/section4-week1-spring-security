package com.codestates.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtTokenizer {

    public String encodeBase64SecretKey(String secretKey) {
        // Plain Text 형태인 Secret Key의 byte[]를 Base64 형식의 문자열로 인코딩
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }



    //인증된 사용자에게 JWT를 최초로 발급해주기 위한 JWT 생성 메서드
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodeSecretKey) {

        //Base64 형식 Secret Key 문자열을 이용해 Key(java.security.Key) 객체를 얻움.
        Key key = getKeyFromBase64EncodeKey(base64EncodeSecretKey);

        return Jwts.builder()
                .setClaims(claims) //JWT에 포함 시킬 Custom Claims를 추가, 주로 인증된 사용자 관련 정보
                .setSubject(subject) // JWT에 대한 제목을 추가
                .setIssuedAt(Calendar.getInstance().getTime()) //JWT 발행 일자를 설정하는데 파라미터 타입은 java.util.Date 타입
                .setExpiration(expiration) //JWT의 만료일시를 지정합니다. 파라미터 타입은 역시 java.util.Date 타입
                .signWith(key) //signWith()에 서명을 위한 Key(java.security.Key) 객체를 설정
                .compact(); //compact()를 통해 JWT를 생성하고 직렬화
    }

    //Access Token이 만료되었을 경우, Access Token을 새로 생성할 수 있게 해주는 Refresh Token을 생성하는 메서드
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodeKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    //verifySignature() 메서드는 Signature를 검증하는 용도이므로 Claims를 리턴할 필요는 없음.
    //파라미터로 사용한 jws는 Signature가 포함된 JWT라는 의미
    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodeKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key) //서명에 사용된 Secret Key를 설정
                .build()
                .parseClaimsJws(jws); //JWT를 파싱해서 Claims를 얻음.
    }




    //여기서만 사용할 매서드 (위 세개의 매서드에서 사용)
    //JWT의 서명에 사용할 Secret Key를 생성
    private Key getKeyFromBase64EncodeKey(String base64EncodedSecretKey) {
        byte[] keybytes = Decoders.BASE64.decode(base64EncodedSecretKey); //Base64 형식으로 인코딩된 Secret Key를 디코딩한 후, byte array를 반환
        Key key = Keys.hmacShaKeyFor(keybytes); //적절한 HMAC 알고리즘을 적용한 Key(java.security.Key) 객체를 생성

        return key;
    }

}
