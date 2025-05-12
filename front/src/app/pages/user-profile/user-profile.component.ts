import { Component, OnInit }                  from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router }             from '@angular/router';
import { MatSnackBar }                        from '@angular/material/snack-bar';
import { forkJoin, of }                       from 'rxjs';
import { switchMap }                          from 'rxjs/operators';

import { AuthService, User } from '../../services/auth.service';
import { ThemeService }      from '../../services/theme.service';
import { ArticleService }    from '../../services/article.service';
import { Subscription }      from '../../models/theme.model';
import { Article }           from '../../models/article.model';

@Component({
  selector   : 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls  : ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  userId       : number                 = 0;
  user         : User | null            = null;
  userForm     : FormGroup;
  subscriptions: Subscription[]         = [];
  themeArticles: Map<number, Article[]> = new Map();
  loading      : boolean                = false;

  constructor(
    private authService   : AuthService,
    private themeService  : ThemeService,
    private articleService: ArticleService,
    private route         : ActivatedRoute,
    private router        : Router,
    private fb            : FormBuilder,
    private snackBar      : MatSnackBar
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required]],
      email   : ['', [Validators.required, Validators.email]],
      password: ['', [Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.userId = currentUser.id;
      this.loadUserProfile();
      this.loadUserSubscriptions();
    }
  }

  loadUserProfile(): void {
    this.authService.getUserProfile(this.userId).subscribe({
      next: (user) => {
        this.user = user;
        this.userForm.patchValue({
          username: user.username,
          email   : user.email,
          password: '' // Le champ de mot de passe reste vide
        });
      },
      error: (error) => {
        this.snackBar.open('Erreur lors du chargement du profil: ' + (error.error?.message || 'Erreur inconnue'), 'Fermer', {
          duration: 5000
        });
      }
    });
  }

  loadUserSubscriptions(): void {
    this.themeService.getUserSubscriptions(this.userId).subscribe({
      next: (subscriptions) => {
        this.subscriptions = subscriptions;
        
        // Chargement des articles pour chaque thème auquel l'utilisateur est abonné
        this.loadSubscribedThemesArticles(subscriptions);
      },
      error: (error) => {
        this.snackBar.open('Erreur lors du chargement des abonnements: ' + (error.error?.message || 'Erreur inconnue'), 'Fermer', {
          duration: 5000
        });
      }
    });
  }
  
  loadSubscribedThemesArticles(subscriptions: Subscription[]): void {
    if (subscriptions.length === 0) {
      return;
    }
    
    // Créer un observable pour chaque thème pour récupérer ses articles
    const observables = subscriptions.map(sub => {
      return this.articleService.getArticlesByTheme(sub.themeId).pipe(
        switchMap(articles => {
          // Stocke les articles par thème
          this.themeArticles.set(sub.themeId, articles);
          return of(articles);
        })
      );
    });
    
    // Execute toutes les requêtes en parallèle
    forkJoin(observables).subscribe({
      error: (error) => {
        console.error('Erreur lors du chargement des articles par thème', error);
      }
    });
  }
  
  getArticlesByTheme(themeId: number): Article[] {
    return this.themeArticles.get(themeId) || [];
  }
  
  navigateToArticle(articleId: number): void {
    this.router.navigate(['/article', articleId]);
  }
  
  navigateToThemes(): void {
    this.router.navigate(['/themes']);
  }

  onSubmitUserForm(): void {
    if (this.userForm.invalid) {
      return;
    }

    this.loading = true;
    const formData = { ...this.userForm.value };
    
    // Si le mot de passe est vide, ne pas l'envoyer à l'API
    if (!formData.password) {
      delete formData.password;
    }

    this.authService.updateUserProfile(this.userId, formData).subscribe({
      next: () => {
        this.loading = false;
        this.snackBar.open('Profil mis à jour avec succès', 'Fermer', {
          duration: 3000
        });
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open('Erreur lors de la mise à jour du profil: ' + (error.error?.message || 'Erreur inconnue'), 'Fermer', {
          duration: 5000
        });
      }
    });
  }

  unsubscribe(themeId: number): void {
    if (confirm('Êtes-vous sûr de vouloir vous désabonner de ce thème ?')) {
      this.themeService.unsubscribeFromTheme(this.userId, themeId).subscribe({
        next: () => {
          this.subscriptions = this.subscriptions.filter(sub => sub.theme.id !== themeId);
          this.snackBar.open('Désabonnement effectué avec succès', 'Fermer', {
            duration: 3000
          });
        },
        error: (error) => {
          this.snackBar.open('Erreur lors du désabonnement: ' + (error.error?.message || 'Erreur inconnue'), 'Fermer', {
            duration: 5000
          });
        }
      });
    }
  }
}
