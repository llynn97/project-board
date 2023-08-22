package com.project.projectboard.dto.response;

import com.project.projectboard.dto.ArticleCommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Getter
@AllArgsConstructor
public class ArticleCommentResponse {

    Long id;
    String content;
    LocalDateTime createdAt;
    String email;
    String nickname;
    String userId;
    Long parentCommentId;
    Set<ArticleCommentResponse> childComments;

    public static ArticleCommentResponse of(Long id,String content,LocalDateTime createdAt,String email,String nickname,String userId){
        return ArticleCommentResponse.of(id,content,createdAt,email,nickname,userId,null);
    }

    public static ArticleCommentResponse of(Long id,String content,LocalDateTime createdAt,String email,String nickname,String userId,Long parentCommentId){
        Comparator<ArticleCommentResponse> childCommentComparator = Comparator
                .comparing(ArticleCommentResponse::getCreatedAt)
                .thenComparing(ArticleCommentResponse::getId);
        return new ArticleCommentResponse(id,content,createdAt,email,nickname,userId,parentCommentId,new TreeSet<>(childCommentComparator));
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto){
        String nickname = dto.getUserAccountDto().getNickname();
        if(nickname == null || nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }
        return ArticleCommentResponse.of(
                dto.getId(),
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getUserAccountDto().getEmail(),
                nickname,
                dto.getUserAccountDto().getUserId(),
                dto.getParentCommentId()
        );
    }

    public boolean hasParentComment(){
        return parentCommentId != null;
    }
}
