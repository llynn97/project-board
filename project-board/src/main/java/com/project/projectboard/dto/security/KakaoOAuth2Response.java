package com.project.projectboard.dto.security;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@AllArgsConstructor
public class KakaoOAuth2Response {

    Long id;
    LocalDateTime connectedAt;
    Map<String, Object> properties;
    KakaoAccount kakaoAccount;



    public static KakaoOAuth2Response from(Map<String, Object> attributes) {
        return new KakaoOAuth2Response(
                Long.valueOf(String.valueOf(attributes.get("id"))),
                LocalDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ),
                (Map<String, Object>) attributes.get("properties"),
                KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account"))
        );
    }

    public String email() { return this.getKakaoAccount().getEmail(); }
    public String nickname() { return this.getKakaoAccount().nickname(); }

}
