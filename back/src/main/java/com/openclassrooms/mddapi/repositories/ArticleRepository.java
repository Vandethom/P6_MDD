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
    List<Article> findByThemesId(Long themeId);
}