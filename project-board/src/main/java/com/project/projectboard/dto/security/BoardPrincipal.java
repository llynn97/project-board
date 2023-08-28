package com.project.projectboard.dto.security;

import com.project.projectboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardPrincipal implements UserDetails, OAuth2User {

    String username;
    String password;
    String email;
    String nickname;
    String memo;
    Collection<? extends GrantedAuthority> authorities;
    Map<String,Object> oauth2Attributes;


    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo){
        return of(username,password,email,nickname,memo,Map.of());
    }

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo, Map<String,Object> oauth2Attributes) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);
        return new BoardPrincipal(
                username,
                password,
                email,
                nickname,
                memo,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                oauth2Attributes
        );
    }

    public static BoardPrincipal from(UserAccountDto dto){
        return BoardPrincipal.of(
               dto.getUserId(),
               dto.getUserPassword(),
               dto.getEmail(),
               dto.getNickname(),
               dto.getMemo()
        );
    }

    public UserAccountDto toDto(){
        return UserAccountDto.of(
                username,
                password,
                email,
                nickname,
                memo
        );
    }


    @Override
    public Map<String, Object> getAttributes() {
        return oauth2Attributes;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public enum RoleType{
        USER("ROLE_USER");

        @Getter private final String name;

        RoleType(String name){
            this.name = name;
        }
    }
}
