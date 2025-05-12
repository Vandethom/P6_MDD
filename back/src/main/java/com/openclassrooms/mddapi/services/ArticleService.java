package com.openclassrooms.mddapi.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleRequest;
import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.ArticleRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ArticleService {
      private final ArticleRepository articleRepository;
    private final UserRepository      userRepository;
    private final ThemeRepository     themeRepository;
    
    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, ThemeRepository themeRepository) {
        this.articleRepository = articleRepository;
        this.userRepository    = userRepository;
        this.themeRepository   = themeRepository;
    }    @Transactional
    public ArticleDTO createArticle(CreateArticleRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new IllegalStateException("No authentication found in security context");
        }
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Creating article for user: " + username);
        
        User currentUser;
        try {
            currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
            System.out.println("Found user with ID: " + currentUser.getId());
        } catch (Exception e) {
            System.err.println("Error finding user by username: " + username + ", Error: " + e.getMessage());
            throw e;
        }
        
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .author(currentUser)
                .themes(new HashSet<>())
                .build();
        
        // Ajouter les thèmes si présents
        if (request.getThemeIds() != null && !request.getThemeIds().isEmpty()) {
            Set<Theme> themes = themeRepository.findAllById(request.getThemeIds())
                    .stream()
                    .collect(Collectors.toSet());
            article.setThemes(themes);
        }
        
        Article savedArticle = articleRepository.save(article);
        return mapToDTO(savedArticle);
    }
    
    @Transactional(readOnly = true)
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        
        return mapToDTO(article);
    }
      @Transactional
    public ArticleDTO updateArticle(Long id, CreateArticleRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        
        // Check if the current user is the author of the article
        if (!article.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalAccessError("You are not authorized to update this article");
        }
        
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setImageUrl(request.getImageUrl());
        
        // Mettre à jour les thèmes si présents
        if (request.getThemeIds() != null) {
            article.getThemes().clear();
            if (!request.getThemeIds().isEmpty()) {
                Set<Theme> themes = themeRepository.findAllById(request.getThemeIds())
                        .stream()
                        .collect(Collectors.toSet());
                article.setThemes(themes);
            }
        }
        
        Article updatedArticle = articleRepository.save(article);
        return mapToDTO(updatedArticle);
    }
    
    @Transactional
    public void deleteArticle(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        
        // Check if the current user is the author of the article
        if (!article.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalAccessError("You are not authorized to delete this article");
        }
        
        articleRepository.delete(article);
    }
      @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByUser(Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        return articleRepository.findByAuthor(author).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByTheme(Long themeId) {
        if (!themeRepository.existsById(themeId)) {
            throw new EntityNotFoundException("Theme not found with id: " + themeId);
        }
        
        return articleRepository.findByThemesId(themeId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
      private ArticleDTO mapToDTO(Article article) {
        Set<ThemeDTO> themeDTOs = article.getThemes().stream()
                .map(theme -> ThemeDTO.builder()
                        .id(theme.getId())
                        .title(theme.getTitle())
                        .description(theme.getDescription())
                        .createdAt(theme.getCreatedAt())
                        .build())
                .collect(Collectors.toSet());
                
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor().getUsername())
                .authorId(article.getAuthor().getId())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .imageUrl(article.getImageUrl())
                .themes(themeDTOs)
                .build();
    }
}