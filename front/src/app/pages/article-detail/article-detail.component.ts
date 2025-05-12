import { Component, OnInit }                  from '@angular/core';
import { ActivatedRoute, Router }             from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar }                        from '@angular/material/snack-bar';
import { Article }                            from '../../models/article.model';
import { Comment }                            from '../../models/comment.model';
import { ArticleService }                     from '../../services/article.service';
import { CommentService }                     from '../../services/comment.service';
import { AuthService }                        from '../../services/auth.service';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {
  article    : Article | null = null;
  comments   : Comment[] = [];
  commentForm: FormGroup;
  loading    = false;
  submitting = false;

  constructor(
    private route         : ActivatedRoute,
    private router        : Router,
    private fb            : FormBuilder,
    private articleService: ArticleService,
    private commentService: CommentService,
    public authService    : AuthService,
    private snackBar      : MatSnackBar
  ) {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  ngOnInit(): void {
    this.loading    = true;
    const articleId = Number(this.route.snapshot.paramMap.get('id'));
    
    if (articleId) {
      this.loadArticleDetails(articleId);
      this.loadArticleComments(articleId);
    } else {
      this.router.navigate(['/home']);
    }
  }

  loadArticleDetails(articleId: number): void {
    this.articleService.getArticle(articleId).subscribe({
      next: (article) => {
        this.article = article;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading article details:', error);
        this.snackBar.open('Erreur lors du chargement de l\'article', 'Fermer', {
          duration: 5000
        });
        this.loading = false;
        this.router.navigate(['/home']);
      }
    });
  }

  loadArticleComments(articleId: number): void {
    this.commentService.getCommentsByArticle(articleId).subscribe({
      next: (comments) => {
        this.comments = comments;
      },
      error: (error) => {
        console.error('Error loading comments:', error);
        this.snackBar.open('Erreur lors du chargement des commentaires', 'Fermer', {
          duration: 5000
        });
      }
    });
  }

  onSubmitComment(): void {
    if (this.commentForm.invalid || !this.article?.id) {
      return;
    }

    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.snackBar.open('Vous devez être connecté pour commenter', 'Fermer', {
        duration: 5000
      });
      return;
    }

    this.submitting = true;
    const newComment: Comment = {
      content: this.commentForm.value.content,
      userId: currentUser.id,
      username: currentUser.username,
      articleId: this.article.id
    };

    this.commentService.addComment(newComment).subscribe({
      next: (comment) => {
        this.comments.unshift(comment);
        this.commentForm.reset();
        this.submitting = false;
      },
      error: (error) => {
        console.error('Error adding comment:', error);
        this.snackBar.open('Erreur lors de l\'ajout du commentaire', 'Fermer', {
          duration: 5000
        });
        this.submitting = false;
      }
    });
  }

  deleteComment(commentId: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce commentaire ?')) {
      this.commentService.deleteComment(commentId).subscribe({
        next: () => {
          this.comments = this.comments.filter(comment => comment.id !== commentId);
          this.snackBar.open('Commentaire supprimé avec succès', 'Fermer', {
            duration: 3000
          });
        },
        error: (error) => {
          console.error('Error deleting comment:', error);
          this.snackBar.open('Erreur lors de la suppression du commentaire', 'Fermer', {
            duration: 5000
          });
        }
      });
    }
  }

  canDeleteComment(comment: Comment): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) return false;
    
    // User can delete their own comments or if they are the article author
    return comment.userId === currentUser.id || 
           (this.article?.authorId === currentUser.id);
  }

  navigateBack(): void {
    this.router.navigate(['/home']);
  }

  // Utility methods for template
  isUserLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  getCurrentUsername(): string | null {
    const currentUser = this.authService.getCurrentUser();
    return currentUser ? currentUser.username : null;
  }
}
