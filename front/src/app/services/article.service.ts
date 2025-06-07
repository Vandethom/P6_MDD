import { Injectable }              from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable }              from 'rxjs';
import { environment }             from '../../environments/environment';
import { Article }                 from '../models/article.model';


export enum SortBy {
  ASC  = 'asc',
  DESC = 'desc'
}

export interface CreateArticleRequest {
  title    : string;
  content  : string;
  imageUrl?: string;
  themeId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private apiUrl = `${environment.apiUrl}/articles`;

  constructor(
    private http       : HttpClient,
  ) { }


  getArticles(sort: SortBy = SortBy.DESC): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}?sort=${sort}`);
  }

  getArticle(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${id}`);
  }
  getArticlesByUser(userId: number, sort: SortBy = SortBy.DESC): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/user/${userId}?sort=${sort}`);
  }

  getArticlesForUserSubscriptions(userId: number, sort: SortBy = SortBy.DESC): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/subscriptions/${userId}?sort=${sort}`);
  }

  getArticlesByTheme(themeId: number, sort: SortBy = SortBy.DESC): Observable<Article[]> {
    return this.http.get<Article[]>(`${environment.apiUrl}/themes/${themeId}/articles?sort=${sort}`);
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