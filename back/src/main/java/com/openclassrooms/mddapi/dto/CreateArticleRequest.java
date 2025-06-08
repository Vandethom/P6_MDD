package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleRequest {
    @NotBlank(message = "Title cannot be empty")
    private String title;    @NotBlank(message = "Content cannot be empty")
    private String content;
    
    @NotNull(message = "Theme ID cannot be null")
    private Long themeId;
}