package com.project.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
public class ArticleComment extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount;

    @Setter
    @Column(nullable = false, length = 500)
    private String content;
    @Setter
    @ManyToOne(optional = false)
    private Article article;


    protected ArticleComment() {
    }

    private ArticleComment(UserAccount userAccount, String content, Article article) {
        this.userAccount = userAccount;
        this.content = content;
        this.article = article;
    }

    public static ArticleComment of(UserAccount userAccount, String content, Article article) {
        return new ArticleComment(userAccount, content, article);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment)) return false;
        ArticleComment that = (ArticleComment) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
