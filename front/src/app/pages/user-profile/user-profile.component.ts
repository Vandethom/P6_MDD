import { Component, OnInit }                  from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router }             from '@angular/router';
import { MatSnackBar }                        from '@angular/material/snack-bar';
import { MatDialog }                          from '@angular/material/dialog';
import { forkJoin, of }                       from 'rxjs';
import { switchMap }                          from 'rxjs/operators';

import { AuthService, User } from '../../services/auth.service';
import { ThemeService }      from '../../services/theme.service';
import { ArticleService, SortBy }    from '../../services/article.service';
import { Subscription }      from '../../models/theme.model';
import { PasswordValidator } from '../../validators/password.validator';
import { Article }           from '../../models/article.model';
import { ConfirmationDialogComponent, ConfirmationDialogData } from '../../components/confirmation-dialog/confirmation-dialog.component';

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
    private snackBar      : MatSnackBar,
    private dialog        : MatDialog
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required]],
      email   : ['', [Validators.required, Validators.email]],
      password: ['', [PasswordValidator.strongPassword()]]
    });
  }

  ngOnInit(): void {
    // Récupérer l'ID depuis les paramètres de la route
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.userId = parseInt(params['id']);
        this.loadUserProfile();
        this.loadUserSubscriptions();
      } else {
        // Si pas d'ID dans la route, utiliser l'utilisateur connecté
        const currentUser = this.authService.getCurrentUser();
        if (currentUser) {
          this.userId = currentUser.id;
          this.loadUserProfile();
          this.loadUserSubscriptions();
        }
      }
    });
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
        console.error('Erreur complète lors du chargement du profil:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        console.error('Error details:', error.error);
        
        let errorMessage = 'Erreur inconnue';
        if (error.status === 404) {
          errorMessage = 'Utilisateur non trouvé';
        } else if (error.status === 401) {
          errorMessage = 'Non autorisé - veuillez vous reconnecter';
        } else if (error.status === 500) {
          errorMessage = 'Erreur serveur';
        } else if (error.error?.message) {
          errorMessage = error.error.message;
        }
        
        this.snackBar.open('Erreur lors du chargement du profil: ' + errorMessage, 'Fermer', {
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
    
    // Créer un observable pour chaque thème pour récupérer ses articles (triés par ordre décroissant par défaut)
    const observables = subscriptions.map(sub => {
      return this.articleService.getArticlesByTheme(sub.themeId, SortBy.DESC).pipe(
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

  getPasswordErrorMessage(): string {
    const passwordControl = this.userForm.get('password');
    if (passwordControl?.errors) {
      return PasswordValidator.getErrorMessage(passwordControl.errors);
    }
    return '';
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

  unsubscribeFromTheme(themeId: number): void {
    // Trouver le nom du thème pour l'afficher dans la confirmation
    const subscription = this.subscriptions.find(sub => sub.themeId === themeId);
    const themeName = subscription?.theme.title || 'ce thème';
    
    const dialogData: ConfirmationDialogData = {
      title: 'Confirmer le désabonnement',
      message: `Êtes-vous sûr de vouloir vous désabonner de "${themeName}" ? Vous ne recevrez plus les nouveaux articles de ce thème.`,
      confirmText: 'Se désabonner',
      cancelText: 'Annuler'
    };

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.performUnsubscribe(themeId);
      }
    });
  }

  private performUnsubscribe(themeId: number): void {
    this.loading = true;
    
    this.themeService.unsubscribeFromTheme(this.userId, themeId).subscribe({
      next: () => {
        // Mettre à jour la liste des abonnements localement
        this.subscriptions = this.subscriptions.filter(sub => sub.themeId !== themeId);
        
        // Supprimer les articles de ce thème de la carte
        this.themeArticles.delete(themeId);
        
        this.loading = false;
        this.snackBar.open('Désabonnement effectué avec succès', 'Fermer', {
          duration: 3000
        });
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open('Erreur lors du désabonnement: ' + (error.error?.message || 'Erreur inconnue'), 'Fermer', {
          duration: 5000
        });
      }
    });
  }
}
