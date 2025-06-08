package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    
    @NotBlank(message = "Title cannot be empty")
    private String title;
    
    @NotBlank(message = "Content cannot be empty")
    private String content;
    
    private String        author;    
    private Long          authorId;    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ThemeDTO      theme;
}