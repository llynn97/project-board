package com.project.projectboard.service;

import com.project.projectboard.domain.Article;
import com.project.projectboard.domain.UserAccount;
import com.project.projectboard.domain.type.SearchType;
import com.project.projectboard.dto.ArticleDto;
import com.project.projectboard.dto.ArticleUpdateDto;
import com.project.projectboard.dto.ArticleWithCommentsDto;
import com.project.projectboard.dto.UserAccountDto;
import com.project.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("비즈니스 로직 - 게시글")
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;
    @Mock
    private ArticleRepository articleRepository;

    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
    }


    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
        Pageable pageable = Pageable.ofSize(20);
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null,pageable);

        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).shouldHaveNoInteractions();

    }

    @DisplayName("없는 해시태그를 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNonexistentHashtag_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
        String hashtagName = "난 없음";
        Pageable pageable = Pageable.ofSize(20);
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtagName,pageable);
    }

    @DisplayName("게시글 ID로 조회하면, 댓글 달긴 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleWithComments_thenReturnsArticleWithComments() {
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);

        assertThat(dto)
                .hasFieldOrPropertyWithValue("title",article.getTitle())
                .hasFieldOrPropertyWithValue("content",article.getContent())
                .hasFieldOrPropertyWithValue("hashtag",article.getHashtag());

        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 달린 게시글이 없으면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticleWithComments_thenThrowsException() {

    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {

    }

    @DisplayName("게시글이 없으면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {

    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {

        ArticleDto dto = createArticleDto("제목","내용","#java");

        given(articleRepository.save(any(Article.class))).willReturn(null);

        sut.saveArticle(dto);

        then(articleRepository).should().save(any(Article.class));
    }




    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        Article article  = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀","새 내용","#springboot");
        given(articleRepository.getReferenceById(dto.getId())).willReturn(article);

        sut.updateArticle(dto);

        assertThat(article)
                .hasFieldOrPropertyWithValue("title",dto.getTitle())
                .hasFieldOrPropertyWithValue("content",dto.getContent())
                .hasFieldOrPropertyWithValue("hashtag",dto.getHashtag());

        then(articleRepository).should().getReferenceById(dto.getId());
    }




    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        willDoNothing().given(articleRepository).delete(any(Article.class));

        sut.deleteArticle(1L);

        then(articleRepository).should().delete(any(Article.class));

    }

    private Article createArticle(Long id) {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content",
                "hashtag"
        );
        return article;
    }
    private Article createArticle() {
        return createArticle(1L);
    }

    private UserAccount createUserAccount() {
        return createUserAccount("uno");
    }

    private UserAccount createUserAccount(String userId) {
        return UserAccount.of(
                userId,
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content","#springboot");
    }

    private ArticleDto createArticleDto(String title, String content,String hashtag) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno"
        );
    }






}
