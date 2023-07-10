package com.project.projectboard.repository;

import com.project.projectboard.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ArticleCommentRepository extends JpaRepository<ArticleComment,Long> {
}
