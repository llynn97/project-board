package com.project.projectboard.dto.response;

import com.project.projectboard.dto.ArticleCommentDto;
import com.project.projectboard.dto.ArticleWithCommentsDto;
import com.project.projectboard.dto.HashtagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ArticleWithCommentsResponse {

    Long id;
    String title;
    String content;
    Set<String> hashtags;
    LocalDateTime createdAt;
    String email;
    String nickname;
    String userId;
    Set<ArticleCommentResponse> articleCommentsResponse;

    public static ArticleWithCommentsResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, String userId, Set<ArticleCommentResponse> articleCommentsResponse){
        return new ArticleWithCommentsResponse(id, title, content, hashtags, createdAt, email, nickname, userId, articleCommentsResponse);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto){
        String nickname = dto.getUserAccountDto().getNickname();
        if(nickname == null || nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }
        return new ArticleWithCommentsResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getContent(),
                dto.getHashtagDtos().stream()
                                .map(HashtagDto::getHashtagName)
                                .collect(Collectors.toUnmodifiableSet()),
                dto.getCreatedAt(),
                dto.getUserAccountDto().getEmail(),
                nickname,
                dto.getUserAccountDto().getUserId(),
                organizeChildComments(dto.getArticleCommentDtos())
        );
    }

    private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> dtos){
        Map<Long,ArticleCommentResponse> map = dtos.stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toMap(ArticleCommentResponse::getId, Function.identity()));

        map.values().stream()
                .filter(ArticleCommentResponse::hasParentComment)
                .forEach(comment -> {
                    ArticleCommentResponse parentComment = map.get(comment.getParentCommentId());
                    parentComment.getChildComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                       new TreeSet<>(
                               Comparator
                                       .comparing(ArticleCommentResponse::getCreatedAt)
                                       .reversed()
                                       .thenComparing(ArticleCommentResponse::getId)
                       )
                ));
    }
}
