import { Component, OnInit } from '@angular/core';
import { Router }            from '@angular/router';
import { MatSnackBar }       from '@angular/material/snack-bar';
import { ArticleService }    from '../../services/article.service';
import { Article }           from '../../models/article.model';
import { AuthService }       from '../../services/auth.service';

@Component({
  selector   : 'app-home',
  templateUrl: './home.component.html',
  styleUrls  : ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  articles       : Article[] = [];
  sortOption     : string    = 'recent';
  isAuthenticated: boolean   = false;
  
  constructor(
    private router        : Router,
    private articleService: ArticleService,
    private authService   : AuthService,
    private snackBar      : MatSnackBar
  ) { }

  ngOnInit(): void {
    this.isAuthenticated = this.authService.isLoggedIn();
    if (this.isAuthenticated) {
      this.loadArticles();
    }
  }
    navigateToLogin(): void {
    this.router.navigate(['/login']);
  }
  
  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }

  loadArticles(): void {
    this.articleService.getArticles().subscribe(
      articles => {
        this.articles = articles;
        this.sortArticles();
      },
      error => {
        console.error('Error loading articles:', error);
      }
    );
  }

  sortArticles(): void {
    switch (this.sortOption) {
      case 'recent':
        this.articles.sort((a, b) => {
          const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
          const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
          return dateB - dateA;
        });
        break;
      case 'oldest':
        this.articles.sort((a, b) => {
          const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
          const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
          return dateA - dateB;
        });
        break;
    }
  }

  createArticle(): void {
    this.router.navigate(['/create-article']);
  }

  editArticle(articleId: number): void {
    this.router.navigate(['/edit-article', articleId]);
  }

  deleteArticle(articleId: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) {
      this.articleService.deleteArticle(articleId).subscribe({
        next: () => {
          this.snackBar.open('Article supprimé avec succès', 'Fermer', {
            duration: 3000
          });
          this.loadArticles();
        },
        error: (error) => {
          this.snackBar.open('Erreur lors de la suppression de l\'article', 'Fermer', {
            duration: 5000
          });
        }
      });
    }
  }

  isCurrentUserArticle(article: Article): boolean {
    const currentUser = this.authService.getCurrentUser();
    return !!currentUser && article.authorId === currentUser.id;
  }
  
  viewArticleDetail(articleId: number): void {
    this.router.navigate(['/article', articleId]);
  }
}
