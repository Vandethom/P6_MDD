package com.openclassrooms.mddapi.services;

import java.util.List;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentRequest;

public interface ICommentService {
    List<CommentDTO> getCommentsByArticle(Long articleId);
    List<CommentDTO> getCommentsByUser(Long userId);
    CommentDTO       createComment(CreateCommentRequest request, Long userId);
    CommentDTO       updateComment(Long commentId, String content, Long userId);
    void             deleteComment(Long commentId, Long userId);
}