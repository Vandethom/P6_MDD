package com.openclassrooms.mddapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleRequest;
import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.ArticleRepository;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;
    
    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, ThemeRepository themeRepository, SubscriptionRepository subscriptionRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
        this.subscriptionRepository = subscriptionRepository;
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

        // Récupérer le thème
        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new EntityNotFoundException("Theme not found with id: " + request.getThemeId()));
        
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .author(currentUser)
                .theme(theme)
                .build();
        
        Article savedArticle = articleRepository.save(article);
        return mapToDTO(savedArticle);
    }
      @Transactional(readOnly = true)
    public List<ArticleDTO> getAllArticles() {
        return getAllArticles("desc");
    }
    
    @Transactional(readOnly = true)
    public List<ArticleDTO> getAllArticles(String sort) {
        List<Article> articles;
        if ("asc".equalsIgnoreCase(sort)) {
            articles = articleRepository.findAllByOrderByCreatedAtAsc();
        } else {
            articles = articleRepository.findAllByOrderByCreatedAtDesc();
        }
        return articles.stream()
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
        
        // Mettre à jour le thème
        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new EntityNotFoundException("Theme not found with id: " + request.getThemeId()));
        article.setTheme(theme);
        
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
    }    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByUser(Long userId) {
        return getArticlesByUser(userId, "desc");
    }
    
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByUser(Long userId, String sort) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        List<Article> articles;
        if ("asc".equalsIgnoreCase(sort)) {
            articles = articleRepository.findByAuthorOrderByCreatedAtAsc(author);
        } else {
            articles = articleRepository.findByAuthorOrderByCreatedAtDesc(author);
        }
        return articles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
      @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByTheme(Long themeId) {
        return getArticlesByTheme(themeId, "desc");
    }
    
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByTheme(Long themeId, String sort) {
        if (!themeRepository.existsById(themeId)) {
            throw new EntityNotFoundException("Theme not found with id: " + themeId);
        }
        
        List<Article> articles;
        if ("asc".equalsIgnoreCase(sort)) {
            articles = articleRepository.findByThemeIdOrderByCreatedAtAsc(themeId);
        } else {
            articles = articleRepository.findByThemeIdOrderByCreatedAtDesc(themeId);
        }
        return articles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }      @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesForUserSubscriptions(Long userId) {
        return getArticlesForUserSubscriptions(userId, "desc");
    }
    
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesForUserSubscriptions(Long userId, String sort) {
        // Vérifier que l'utilisateur existe
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        // Récupérer les abonnements de l'utilisateur
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        
        // Si l'utilisateur n'a aucun abonnement, retourner une liste vide
        if (subscriptions.isEmpty()) {
            return List.of();
        }
        
        // Extraire les IDs des thèmes auxquels l'utilisateur est abonné
        List<Long> themeIds = subscriptions.stream()
                .map(subscription -> subscription.getTheme().getId())
                .collect(Collectors.toList());
          
        // Récupérer les articles des thèmes abonnés, triés selon le paramètre
        List<Article> articles;
        if ("asc".equalsIgnoreCase(sort)) {
            articles = articleRepository.findByThemeIdInOrderByCreatedAtAsc(themeIds);
        } else {
            articles = articleRepository.findByThemeIdInOrderByCreatedAtDesc(themeIds);
        }
        return articles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    private ArticleDTO mapToDTO(Article article) {
        ThemeDTO themeDTO = null;
        if (article.getTheme() != null) {
            themeDTO = ThemeDTO.builder()
                    .id(article.getTheme().getId())
                    .title(article.getTheme().getTitle())
                    .description(article.getTheme().getDescription())
                    .createdAt(article.getTheme().getCreatedAt())
                    .build();
        }
                
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor().getUsername())
                .authorId(article.getAuthor().getId())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .imageUrl(article.getImageUrl())
                .theme(themeDTO)
                .build();
    }
}