package com.project.projectboard.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class KakaoAccount {

    Boolean profileNicknameNeedsAgreement;
    Profile profile;
    Boolean hasEmail;
    Boolean emailNeedsAgreement;
    Boolean isEmailValid;
    Boolean isEmailVerified;
    String email;

    public static KakaoAccount from(Map<String, Object> attributes) {
        return new KakaoAccount(
                Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement"))),
                Profile.from((Map<String, Object>) attributes.get("profile")),
                Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                String.valueOf(attributes.get("email"))
        );
    }

    public String nickname() { return this.getProfile().getNickname(); }
}
