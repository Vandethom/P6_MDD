package com.openclassrooms.mddapi.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleRequest {
    @NotBlank(message = "Title cannot be empty")
    private String title;
      @NotBlank(message = "Content cannot be empty")
    private String    content;
    private String    imageUrl;
    private Set<Long> themeIds;
}