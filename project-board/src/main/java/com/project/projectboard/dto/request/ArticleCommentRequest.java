package com.project.projectboard.dto.request;

import com.project.projectboard.dto.ArticleCommentDto;
import com.project.projectboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleCommentRequest {

    private Long articleId;
    private String content;
    private Long parentCommentId;

    public static ArticleCommentRequest of(Long articleId , String content){
        return new ArticleCommentRequest(articleId,content,null);
    }
    public static ArticleCommentRequest of(Long articleId , String content, Long parentCommentId){
        return new ArticleCommentRequest(articleId,content,parentCommentId);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto){
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content,
                parentCommentId
        );
    }
}
