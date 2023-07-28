package com.project.projectboard.controller;

import com.project.projectboard.dto.UserAccountDto;
import com.project.projectboard.dto.request.ArticleCommentRequest;
import com.project.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest){
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of("uno","{noop}asdf1234","uno@mail.com","Uno","memo")));
        return "redirect:/articles/" + articleCommentRequest.getArticleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId){
        articleCommentService.deleteArticleComment(commentId);
        return "redirect:/articles/" + articleId;
    }
}
