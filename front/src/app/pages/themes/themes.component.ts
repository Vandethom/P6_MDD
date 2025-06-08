import { Component, OnInit }   from '@angular/core';
import { Theme, Subscription } from '../../models/theme.model';
import { ThemeService }        from '../../services/theme.service';
import { AuthService }         from '../../services/auth.service';
import { MatSnackBar }         from '@angular/material/snack-bar';
import { Router }              from '@angular/router';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  themes             : Theme[]  = [];
  subscribedThemesIds: number[] = [];
  loading                       = false;
  errorMessage                  = '';
  successMessage                = '';
  
  constructor(
    private themeService: ThemeService,
    private authService : AuthService,
    private snackBar    : MatSnackBar,
    private router      : Router
  ) { }

  ngOnInit(): void {
    this.loadThemes();
    this.loadUserSubscriptions();
  }

  loadThemes(): void {
    this.loading = true;
    this.themeService.getThemes().subscribe({
      next: (themes) => {
        this.themes = themes;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des thèmes', error);
        this.errorMessage = 'Erreur lors du chargement des thèmes. Veuillez réessayer.';
        this.loading      = false;
      }
    });
  }

  loadUserSubscriptions(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.themeService.getUserSubscriptions(currentUser.id).subscribe({
        next: (subscriptions) => {
          this.subscribedThemesIds = subscriptions.map(sub => sub.themeId);
        },
        error: (error) => {
          console.error('Erreur lors du chargement des abonnements', error);
        }
      });
    }
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemesIds.includes(themeId);
  }

  subscribeToTheme(themeId: number): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id) {
      this.snackBar.open('Vous devez être connecté pour vous abonner à un thème', 'Fermer', {
        duration: 3000
      });
      return;
    }

    // Ne pas permettre l'abonnement si déjà abonné
    if (this.isSubscribed(themeId)) {
      return;
    }

    this.loading = true;
    
    // Abonnement uniquement
    this.themeService.subscribeToTheme(currentUser.id, themeId).subscribe({
      next: () => {
        this.subscribedThemesIds.push(themeId);
        this.snackBar.open('Abonnement effectué avec succès', 'Fermer', {
          duration: 3000
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors de l\'abonnement', error);
        this.snackBar.open('Erreur lors de l\'abonnement', 'Fermer', {
          duration: 5000
        });
        this.loading = false;
      }
    });
  }

  viewUserProfile(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.router.navigate(['/user', currentUser.id]);
    }
  }
}
