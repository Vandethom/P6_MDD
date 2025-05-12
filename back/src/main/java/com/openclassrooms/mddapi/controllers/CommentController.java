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

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.services.AuthService;
import com.openclassrooms.mddapi.services.CommentService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthService    authService;
    
    @Autowired
    public CommentController(CommentService commentService, AuthService authService) {
        this.commentService = commentService;
        this.authService    = authService;
    }
    
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByArticle(@PathVariable Long articleId) {
        List<CommentDTO> comments = commentService.getCommentsByArticle(articleId);

        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUser(userId);

        return ResponseEntity.ok(comments);
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CreateCommentRequest request) {
        try {
            Authentication auth           = SecurityContextHolder.getContext().getAuthentication();
            Long           userId         = authService.getUserIdFromAuthentication(auth);
            CommentDTO     createdComment = commentService.createComment(request, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long id, 
            @RequestBody String content) {
        try {
            Authentication auth           = SecurityContextHolder.getContext().getAuthentication();
            Long           userId         = authService.getUserIdFromAuthentication(auth);
            CommentDTO     updatedComment = commentService.updateComment(id, content, userId);

            return ResponseEntity.ok(updatedComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            Authentication auth   = SecurityContextHolder.getContext().getAuthentication();
            Long           userId = authService.getUserIdFromAuthentication(auth);
            
            commentService.deleteComment(id, userId);

            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}