package com.openclassrooms.mddapi.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.ArticleRepository;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final UserRepository    userRepository;
    private final ArticleRepository articleRepository;
    
    @Autowired
    public CommentService(
            CommentRepository commentRepository, 
            UserRepository    userRepository, 
            ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository    = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<CommentDTO> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByUser(Long userId) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(CreateCommentRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setArticle(article);
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    @Override
    public CommentDTO updateComment(Long commentId, String content, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        
        // Check if the user is the author of the comment
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User cannot update this comment");
        }
        
        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        
        // Check if the user is the author of the comment or the article
        if (!comment.getUser().getId().equals(userId) && 
            !comment.getArticle().getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("User cannot delete this comment");
        }
        
        commentRepository.delete(comment);
    }
    
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUsername(comment.getUser().getUsername());
        dto.setUserId(comment.getUser().getId());
        dto.setArticleId(comment.getArticle().getId());
        return dto;
    }
}