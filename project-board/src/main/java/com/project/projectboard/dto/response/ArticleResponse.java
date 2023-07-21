package com.project.projectboard.dto.response;

import com.project.projectboard.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleResponse {

    Long id;
    String title;
    String content;
    String hashtag;
    LocalDateTime createdAt;
    String email;
    String nickname;

    public static ArticleResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, hashtag, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();
        ;
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.getUserAccountDto().getUserId();
        }

        return new ArticleResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getContent(),
                dto.getHashtag(),
                dto.getCreatedAt(),
                dto.getUserAccountDto().getEmail(),
                nickname
        );
    }

}
