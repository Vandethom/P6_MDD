package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private Long userId;
    private Long articleId;

    // Constructors
    public CommentDTO() {
    }

    public CommentDTO(
        Long id, 
        String content, 
        LocalDateTime createdAt, 
        String username, 
        Long userId, 
        Long articleId
        ) {
        this.id        = id;
        this.content   = content;
        this.createdAt = createdAt;
        this.username  = username;
        this.userId    = userId;
        this.articleId = articleId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}