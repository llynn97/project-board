package com.project.projectboard.dto.request;

import com.project.projectboard.dto.ArticleDto;
import com.project.projectboard.dto.HashtagDto;
import com.project.projectboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class ArticleRequest {

    private String title;
    private String content;

    public static ArticleRequest of(String title, String content){
        return new ArticleRequest(title,content);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto){
        return toDto(userAccountDto,null);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos){
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtagDtos
        );
    }
}
