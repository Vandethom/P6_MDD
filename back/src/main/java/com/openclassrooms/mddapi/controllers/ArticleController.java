package com.openclassrooms.mddapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleRequest;
import com.openclassrooms.mddapi.services.ArticleService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    
    private final ArticleService articleService;
    
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        try {
            ArticleDTO article = articleService.getArticleById(id);
            return ResponseEntity.ok(article);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
      @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createArticle(@Valid @RequestBody CreateArticleRequest request) {
        try {
            System.out.println("Received request to create article: " + request.getTitle());
            
            // Check if authentication exists
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                System.err.println("No authentication found or user not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            
            System.out.println("Authenticated user: " + auth.getName());
            
            ArticleDTO createdArticle = articleService.createArticle(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
        } catch (Exception e) {
            System.err.println("Error creating article: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating article: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleDTO> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody CreateArticleRequest request) {
        try {
            ArticleDTO updatedArticle = articleService.updateArticle(id, request);
            return ResponseEntity.ok(updatedArticle);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByUser(@PathVariable Long userId) {
        try {
            List<ArticleDTO> articles = articleService.getArticlesByUser(userId);
            return ResponseEntity.ok(articles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}