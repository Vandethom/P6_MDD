import { Component, OnInit } from '@angular/core';
import { Theme }             from 'src/app/models/theme.model';
import { ThemeService }      from 'src/app/services/theme.service';
import { AuthService }       from 'src/app/services/auth.service';

@Component({
  selector: 'app-theme-subscription',
  templateUrl: './theme-subscription.component.html',
  styleUrls: ['./theme-subscription.component.scss']
})
export class ThemeSubscriptionComponent implements OnInit {
  availableThemes    : Theme[]  = [];
  subscribedThemesIds: number[] = [];
  isLoading                     = false;
  errorMessage                  = '';
  successMessage                = '';

  constructor(
    private themeService: ThemeService,
    private authService : AuthService
  ) { }

  ngOnInit(): void {
    this.loadThemes();
    this.loadUserSubscriptions();
  }
  loadThemes(): void {
    this.isLoading = true;
    this.themeService.getThemes().subscribe({
      next: (themes) => {
        this.availableThemes = themes;
        this.isLoading       = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des thèmes', error);
        this.errorMessage = 'Erreur lors du chargement des thèmes. Veuillez réessayer.';
        this.isLoading    = false;
      }
    });
  }
  loadUserSubscriptions(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      const userId = currentUser.id;
      this.themeService.getUserSubscriptions(userId).subscribe({
        next: (subscriptions) => {
          this.subscribedThemesIds = subscriptions.map(sub => sub.themeId);
        },
        error: (error) => {
          console.error('Erreur lors du chargement des abonnements', error);
          this.errorMessage = 'Erreur lors du chargement des abonnements. Veuillez réessayer.';
        }
      });
    }
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemesIds.includes(themeId);
  }
  toggleSubscription(themeId: number): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id) {
      this.errorMessage = 'Vous devez être connecté pour vous abonner à un thème.';
      return;
    }
    const userId = currentUser.id;

    this.isLoading      = true;
    this.errorMessage   = '';
    this.successMessage = '';

    if (this.isSubscribed(themeId)) {
      // Désabonnement
      this.themeService.unsubscribeFromTheme(userId, themeId).subscribe({
        next: () => {
          this.subscribedThemesIds = this.subscribedThemesIds.filter(id => id !== themeId);
          this.successMessage = 'Désabonnement réussi.';
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erreur lors du désabonnement', error);
          this.errorMessage = 'Erreur lors du désabonnement. Veuillez réessayer.';
          this.isLoading = false;
        }
      });
    } else {
      // Abonnement
      this.themeService.subscribeToTheme(userId, themeId).subscribe({
        next: () => {
          this.subscribedThemesIds.push(themeId);
          this.successMessage = 'Abonnement réussi.';
          this.isLoading      = false;
        },
        error: (error) => {
          console.error('Erreur lors de l\'abonnement', error);
          this.errorMessage = 'Erreur lors de l\'abonnement. Veuillez réessayer.';
          this.isLoading    = false;
        }
      });
    }
  }
}
