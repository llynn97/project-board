package com.project.projectboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ArticleWithCommentsDto {

    Long id;
    UserAccountDto userAccountDto;
    Set<ArticleCommentDto> articleCommentDtos;
    String title;
    String content;
    String hashtag;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;

    public static ArticleWithCommentsDto of(Long id, UserAccountDto userAccountDto, Set<ArticleCommentDto> articleCommentDtos, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy){
        return new ArticleWithCommentsDto(id,userAccountDto,articleCommentDtos,title,content,hashtag,createdAt,createdBy,modifiedAt,modifiedBy);
    }
}
