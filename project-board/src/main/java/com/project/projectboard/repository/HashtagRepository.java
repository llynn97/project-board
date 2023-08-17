package com.project.projectboard.repository;

import com.project.projectboard.domain.Hashtag;
import com.project.projectboard.domain.QHashtag;
import com.project.projectboard.repository.querydsl.HashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HashtagRepository extends
        JpaRepository<Hashtag,Long>,
        HashtagRepositoryCustom,
        QuerydslPredicateExecutor<Hashtag>,
        QuerydslBinderCustomizer<QHashtag> {
    Optional<Hashtag> findByHashtagName(String hashtagName);
    List<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);

}
