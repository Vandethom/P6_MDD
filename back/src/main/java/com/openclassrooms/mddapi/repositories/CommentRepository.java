package com.openclassrooms.mddapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId);
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);
}