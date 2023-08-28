package com.project.projectboard.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Profile {

    String nickname;

    public static Profile from(Map<String, Object> attributes) {
        return new Profile(String.valueOf(attributes.get("nickname")));
    }
}
