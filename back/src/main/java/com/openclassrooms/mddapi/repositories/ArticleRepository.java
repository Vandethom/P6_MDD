package com.openclassrooms.mddapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthor(User author);
    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findAllByOrderByCreatedAtAsc();
    List<Article> findByThemeId(Long themeId);
    List<Article> findByThemeIdInOrderByCreatedAtDesc(List<Long> themeIds);
    List<Article> findByThemeIdInOrderByCreatedAtAsc(List<Long> themeIds);
    List<Article> findByAuthorOrderByCreatedAtDesc(User author);
    List<Article> findByAuthorOrderByCreatedAtAsc(User author);
    List<Article> findByThemeIdOrderByCreatedAtDesc(Long themeId);
    List<Article> findByThemeIdOrderByCreatedAtAsc(Long themeId);
}