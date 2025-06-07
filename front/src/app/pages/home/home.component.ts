import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router }            from '@angular/router';
import { MatSnackBar }       from '@angular/material/snack-bar';
import { ArticleService, SortBy } from '../../services/article.service';
import { Article }           from '../../models/article.model';
import { AuthService }       from '../../services/auth.service';
import { Subscription }      from 'rxjs';

@Component({
  selector   : 'app-home',
  templateUrl: './home.component.html',
  styleUrls  : ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  articles       : Article[] = [];
  sortOption     : string    = 'recent';
  isAuthenticated: boolean   = false;
  private authSubscription: Subscription = new Subscription();
  
  constructor(
    private router        : Router,
    private articleService: ArticleService,
    private authService   : AuthService,
    private snackBar      : MatSnackBar
  ) { }
  ngOnInit(): void {
    // S'abonner aux changements d'authentification
    this.authSubscription = this.authService.isAuthenticated$.subscribe(isAuth => {
      this.isAuthenticated = isAuth;
      if (this.isAuthenticated) {
        this.loadArticles();
      } else {
        this.articles = [];
      }
    });
  }

  ngOnDestroy(): void {
    this.authSubscription.unsubscribe();
  }
    navigateToLogin(): void {
    this.router.navigate(['/login']);
  }
  
  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }  loadArticles(): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      console.error('No current user found');
      return;
    }

    // Convertir sortOption en paramètre de tri
    const sortValue = this.sortOption === 'recent' ? SortBy.DESC : SortBy.ASC;

    this.articleService.getArticlesForUserSubscriptions(currentUser.id, sortValue).subscribe(
      articles => {
        this.articles = articles;
        // Plus besoin de tri côté client car c'est fait côté serveur
      },
      error => {
        console.error('Error loading articles:', error);
        this.snackBar.open('Erreur lors du chargement des articles', 'Fermer', {
          duration: 5000
        });
      }
    );
  }

  sortArticles(): void {
    // Recharger les articles avec le nouveau tri
    this.loadArticles();
  }

  createArticle(): void {
    this.router.navigate(['/create-article']);
  }

  navigateToThemes(): void {
    this.router.navigate(['/themes']);
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
