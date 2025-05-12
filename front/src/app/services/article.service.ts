import { Injectable }              from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable }              from 'rxjs';
import { environment }             from '../../environments/environment';
import { AuthService }             from './auth.service';
import { Article }                 from '../models/article.model';

export interface CreateArticleRequest {
  title    : string;
  content  : string;
  imageUrl?: string;
  themeIds?: number[];
}

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private apiUrl = `${environment.apiUrl}/articles`;

  constructor(
    private http       : HttpClient,
    private authService: AuthService
  ) { }

  getArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(this.apiUrl);
  }

  getArticle(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${id}`);
  }

  getArticlesByUser(userId: number): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/user/${userId}`);
  }

  getArticlesByTheme(themeId: number): Observable<Article[]> {
    return this.http.get<Article[]>(`${environment.apiUrl}/themes/${themeId}/articles`);
  }

  createArticle(articleRequest: CreateArticleRequest): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, articleRequest);
  }

  updateArticle(id: number, articleRequest: CreateArticleRequest): Observable<Article> {
    return this.http.put<Article>(`${this.apiUrl}/${id}`, articleRequest);
  }

  deleteArticle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}