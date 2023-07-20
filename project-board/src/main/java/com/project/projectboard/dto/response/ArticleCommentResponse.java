package com.project.projectboard.dto.response;

import com.project.projectboard.dto.ArticleCommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class ArticleCommentResponse {

    Long id;
    String content;
    LocalDateTime createdAt;
    String email;
    String nickname;

    public static ArticleCommentResponse of(Long id,String content,LocalDateTime createdAt,String email,String nickname){
        return new ArticleCommentResponse(id,content,createdAt,email,nickname);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto){
        String nickname = dto.getUserAccountDto().getNickname();
        if(nickname == null || nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }
        return new ArticleCommentResponse(
                dto.getId(),
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getUserAccountDto().getEmail(),
                nickname
        );
    }
}
