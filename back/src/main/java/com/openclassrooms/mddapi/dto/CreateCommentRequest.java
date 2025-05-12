package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCommentRequest {
    @NotBlank
    private String content;
    
    @NotNull
    private Long articleId;

    // Constructors
    public CreateCommentRequest() {
    }

    public CreateCommentRequest(String content, Long articleId) {
        this.content   = content;
        this.articleId = articleId;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}