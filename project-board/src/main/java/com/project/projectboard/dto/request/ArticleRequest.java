package com.project.projectboard.dto.request;

import com.project.projectboard.dto.ArticleDto;
import com.project.projectboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleRequest {

    private String title;
    private String content;
    private String hashtag;

    public static ArticleRequest of(String title, String content, String hashtag){
        return new ArticleRequest(title,content,hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto){
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtag
        );
    }
}
