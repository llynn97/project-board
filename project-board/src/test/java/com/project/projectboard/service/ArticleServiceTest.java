package com.project.projectboard.service;

import com.project.projectboard.domain.Article;
import com.project.projectboard.domain.Hashtag;
import com.project.projectboard.domain.UserAccount;
import com.project.projectboard.domain.type.SearchType;
import com.project.projectboard.dto.ArticleDto;
import com.project.projectboard.dto.ArticleWithCommentsDto;
import com.project.projectboard.dto.HashtagDto;
import com.project.projectboard.dto.UserAccountDto;
import com.project.projectboard.repository.ArticleRepository;
import com.project.projectboard.repository.HashtagRepository;
import com.project.projectboard.repository.UserAccountRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

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

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private HashtagService hashtagService;

    @Mock
    private HashtagRepository hashtagRepository;




    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
        Pageable pageable = Pageable.ofSize(20);

        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null, pageable);

        // Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(hashtagRepository).shouldHaveNoInteractions();
        then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("없는 해시태그를 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNonexistentHashtag_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
        // Given
        String hashtagName = "난 없지롱";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByHashtagNames(List.of(hashtagName), pageable)).willReturn(new PageImpl<>(List.of(), pageable, 0));

        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtagName, pageable);

        // Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtagNames(List.of(hashtagName), pageable);
    }

    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchingArticlesViaHashtag_thenReturnsArticlesPage() {
        // Given
        String hashtagName = "java";
        Pageable pageable = Pageable.ofSize(20);
        Article expectedArticle = createArticle();

        given(articleRepository.findByHashtagNames(List.of(hashtagName), pageable)).willReturn(new PageImpl<>(List.of(expectedArticle), pageable, 1));

        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtagName, pageable);

        // Then
        assertThat(articles).isEqualTo(new PageImpl<>(List.of(ArticleDto.from(expectedArticle)), pageable, 1));
        then(articleRepository).should().findByHashtagNames(List.of(hashtagName),pageable);
    }

    @Test
    @DisplayName("해시태그를 조회하면 유니크 해시태그 리스트를 반환한다.")
    void givenNothing_whenCalling_thenReturnHashtag(){
        Article article = createArticle();
        List<String> expectedHashtags = List.of("#java","#spring","#boot");
        given(hashtagRepository.findAllHashtagNames()).willReturn(expectedHashtags);

        List<String> actualHashtags = sut.getHashtags();

        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(hashtagRepository).should().findAllHashtagNames();

    }

    @DisplayName("게시글 ID로 조회하면, 댓글 달긴 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleWithComments_thenReturnsArticleWithComments() {
        // Given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        // When
        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);
        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent());
               //.hasFieldOrPropertyWithValue("hashtagDtos", article.getHashtags().stream()
               //         .map(HashtagDto::from)
               //         .collect(Collectors.toUnmodifiableSet())
               // );
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);

        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent());
                //.hasFieldOrPropertyWithValue("hashtagDtos",
                //        article.getHashtags().stream()
                //        .map(HashtagDto::from).collect(Collectors.toUnmodifiableSet())
                // );
        then(articleRepository).should().findById(articleId);

    }

    @DisplayName("게시글 정보를 입력하면, 본문에서 해시태그 정보를 추출하여 해시태그 정보가 포함된 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenExtractsHashtagsFromContentAndSavesArticleWithExtractedHashtags() {

        ArticleDto dto = createArticleDto();
        Set<String> expectedHashtagNames = Set.of("java","spring");
        Set<Hashtag> expectedHashtags = new LinkedHashSet<>();
        expectedHashtags.add(createHashtag("java"));


        given(userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId())).willReturn(createUserAccount());
        given(hashtagService.parseHashtagNames(dto.getContent())).willReturn(expectedHashtagNames);
        given(hashtagService.findHashtagsByNames(expectedHashtagNames)).willReturn(expectedHashtags);
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());

        sut.saveArticle(dto);

        then(userAccountRepository).should().getReferenceById(dto.getUserAccountDto().getUserId());
        then(hashtagService).should().parseHashtagNames(dto.getContent());
        then(hashtagService).should().findHashtagsByNames(expectedHashtagNames);
        then(articleRepository).should().save(any(Article.class));

    }


    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용 #springboot");
        Set<String> expectedHashtagNames = Set.of("springboot");
        Set<Hashtag> expectedHashtags = new HashSet<>();

        given(articleRepository.getReferenceById(dto.getId())).willReturn(article);
        given(userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId())).willReturn(dto.getUserAccountDto().toEntity());
        given(hashtagService.parseHashtagNames(dto.getContent())).willReturn(expectedHashtagNames);
        given(hashtagService.findHashtagsByNames(expectedHashtagNames)).willReturn(expectedHashtags);

        sut.updateArticle(dto.getId(),dto);

        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.getTitle())
                .hasFieldOrPropertyWithValue("content", dto.getContent())
                .extracting("hashtags", as(InstanceOfAssertFactories.COLLECTION))
                .hasSize(1)
                .extracting("hashtagName")
                .containsExactly("springboot");


        then(articleRepository).should().getReferenceById(dto.getId());
        then(userAccountRepository).should().getReferenceById(dto.getUserAccountDto().getUserId());
        then(articleRepository).should().flush();
        then(hashtagService).should(times(2)).deleteHashtagWithoutArticles(any());
        then(hashtagService).should().parseHashtagNames(dto.getContent());
        then(hashtagService).should().findHashtagsByNames(expectedHashtagNames);
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        ArticleDto dto = createArticleDto("새 타이틀", "내용");
        given(articleRepository.getReferenceById(dto.getId())).willThrow(EntityNotFoundException.class);

        sut.updateArticle(1L,dto);

        then(articleRepository).should().getReferenceById(dto.getId());
        then(userAccountRepository).shouldHaveNoInteractions();
        then(hashtagService).shouldHaveNoInteractions();
    }

    @DisplayName("게시글 작성자가 아닌 사람이 수정 정보를 입력하면, 아무 것도 하지 않는다.")
    @Test
    void givenModifiedArticleInfoWithDifferentUser_whenUpdatingArticle_thenDoesNothing(){
        Long differentArticleId = 22L;
        Article differentArticle = createArticle(differentArticleId);
        differentArticle.setUserAccount(createUserAccount("John"));
        ArticleDto dto = createArticleDto("새 타이틀","새 내용");

        given(articleRepository.getReferenceById(differentArticleId)).willReturn(differentArticle);
        given(userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId())).willReturn(dto.getUserAccountDto().toEntity());

        sut.updateArticle(differentArticleId,dto);

        then(articleRepository).should().getReferenceById(differentArticleId);
        then(userAccountRepository).should().getReferenceById(dto.getUserAccountDto().getUserId());
        then(hashtagService).shouldHaveNoInteractions();
    }


    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        Long articleId = 1L;
        String userId= "uno";
        given(articleRepository.getReferenceById(articleId)).willReturn(createArticle());
        willDoNothing().given(articleRepository).deleteByIdAndUserAccount_UserId(articleId,userId);
        willDoNothing().given(articleRepository).flush();
        willDoNothing().given(hashtagService).deleteHashtagWithoutArticles(any());

        sut.deleteArticle(1L,userId);

        then(articleRepository).should().getReferenceById(articleId);
        then(articleRepository).should().deleteByIdAndUserAccount_UserId(articleId,userId);
        then(articleRepository).should().flush();
        then(hashtagService).should(times(2)).deleteHashtagWithoutArticles(any());
    }

    private Article createArticle(Long id) {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );
        article.addHashtags(Set.of(
                createHashtag(1L,"java"),
                createHashtag(2L,"spring")
                ));
        ReflectionTestUtils.setField(article,"id",id);
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
        return createArticleDto("title", "content");
    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno"
        );
    }

    private Hashtag createHashtag(String hashtagName) {
        return createHashtag(1L, hashtagName);
    }

    private Hashtag createHashtag(Long id, String hashtagName) {
        Hashtag hashtag = Hashtag.of(hashtagName);
        ReflectionTestUtils.setField(hashtag, "id", id);

        return hashtag;
    }

    private HashtagDto createHashtagDto() {
        return HashtagDto.of("java");
    }


}
