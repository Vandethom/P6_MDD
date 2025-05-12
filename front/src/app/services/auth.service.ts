import { Injectable }  from '@angular/core';
import { HttpClient }  from '@angular/common/http';
import { Observable }  from 'rxjs';
import { tap }         from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface AuthResponse {
  token   : string;
  username: string;
  userId  : number;
}

interface LoginRequest {
  username: string;
  password: string;
}

interface RegisterRequest {
  username: string;
  password: string;
  email   : string;
}

interface DecodedToken {
  subject       : string;
  expirationTime: number;
  issuedAtTime  : number;
}

export interface User {
  id      : number;
  username: string;
  email?  : string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  login(
    username: string, 
    password: string
  ): Observable<AuthResponse> {
      const loginRequest: LoginRequest = { username, password };
      return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, loginRequest)
        .pipe(
          tap(response => {
            this.storeAuthData(response);
          })
        );
    }

  register(
    username: string, 
    password: string, 
    email   : string): Observable<AuthResponse> {
      const registerRequest: RegisterRequest = { username, password, email };
      return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, registerRequest)
        .pipe(
          tap(response => {
            this.storeAuthData(response);
          })
        );
    }

  private storeAuthData(response: AuthResponse): void {
    localStorage.setItem('token',    response.token);
    localStorage.setItem('username', response.username);
    localStorage.setItem('userId',   response.userId.toString());
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('userId');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    console.log('Token exists:', !!token);
    
    if (token) {
      const isExpired = this.isTokenExpired(token);
      console.log('Token expired:', isExpired);
      return !isExpired;
    }
    
    return false;
  }

  isTokenExpired(token: string): boolean {
    try {
      const decoded = this.decodeToken(token);
      if (!decoded || !decoded.expirationTime) {
        return true;
      }
      // exp is in seconds, convert to milliseconds
      const expirationDate = new Date(decoded.expirationTime * 1000);
      return expirationDate.getTime() < new Date().getTime();
    } catch (error) {
      return true;
    }
  }

  private decodeToken(token: string): DecodedToken | null {
    try {
      // Browser-compatible JWT decoding
      const base64Url   = token.split('.')[1];
      const base64      = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      const rawToken = JSON.parse(jsonPayload);
      return {
        subject       : rawToken.sub,
        expirationTime: rawToken.exp,
        issuedAtTime  : rawToken.iat
      };
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  getUserId(): string | null {
    return localStorage.getItem('userId');
  }
  getCurrentUser(): User | null {
    const userId   = this.getUserId();
    const username = this.getUsername();
    
    if (userId && username) {
      return {
        id      : parseInt(userId),
        username: username
      };
    }
    
    return null;
  }
  
    // Récupérer le profil complet d'un utilisateur par ID
  getUserProfile(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${userId}`);
  }
  
  // Mettre à jour le profil utilisateur
  updateUserProfile(userId: number, userData: { username?: string; email?: string; password?: string }): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/${userId}`, userData)
      .pipe(
        tap(response => {
          if (response.username) {
            localStorage.setItem('username', response.username);
          }
        })
      );
  }
}