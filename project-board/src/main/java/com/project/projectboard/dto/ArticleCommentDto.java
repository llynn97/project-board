package com.project.projectboard.dto;

import com.project.projectboard.domain.Article;
import com.project.projectboard.domain.ArticleComment;
import com.project.projectboard.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleCommentDto {

    Long id;
    Long articleId;
    UserAccountDto userAccountDto;
    String content;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;
    Long parentCommentId;

    public static ArticleCommentDto of(Long articleId, UserAccountDto userAccountDto, String content) {
        return ArticleCommentDto.of(articleId, userAccountDto, content,null);
    }

    public static ArticleCommentDto of(Long articleId, UserAccountDto userAccountDto, String content, Long parentCommentId){
        return ArticleCommentDto.of(null,articleId,userAccountDto,content,null,null,null,null,parentCommentId);
    }

    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy, Long parentCommentId) {
        return new ArticleCommentDto(id, articleId, userAccountDto, content, createdAt, createdBy, modifiedAt, modifiedBy,parentCommentId);
    }



    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                entity.getParentCommentId()

        );
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount){
        return ArticleComment.of(
                userAccount,
                content,
                article
        );
    }


}
