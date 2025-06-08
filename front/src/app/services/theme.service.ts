import { Injectable }          from '@angular/core';
import { HttpClient }          from '@angular/common/http';
import { Observable }          from 'rxjs';
import { environment }         from '../../environments/environment';
import { Theme, Subscription } from '../models/theme.model';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Récupérer tous les thèmes
  getThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/themes`);
  }

  // Récupérer un thème par ID
  getTheme(id: number): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/themes/${id}`);
  }

  // Récupérer les thèmes auxquels un utilisateur est abonné
  getUserSubscriptions(userId: number): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(`${this.apiUrl}/users/${userId}/subscriptions`);
  }  // S'abonner à un thème
  subscribeToTheme(userId: number, themeId: number): Observable<Subscription> {
    return this.http.post<Subscription>(`${this.apiUrl}/users/${userId}/subscriptions`, { themeId });
  }

  // Désabonnement désactivé dans la version MVP
  // unsubscribeFromTheme(userId: number, themeId: number): Observable<void> {
  //   return this.http.delete<void>(`${this.apiUrl}/users/${userId}/subscriptions/${themeId}`);
  // }
}
