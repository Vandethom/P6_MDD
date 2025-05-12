package com.openclassrooms.mddapi.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String title;
    
    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
      @Column(name = "image_url")
    private String imageUrl;    @Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "article_themes",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "theme_id")    )
    private Set<Theme> themes = new HashSet<>();    @Default
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}