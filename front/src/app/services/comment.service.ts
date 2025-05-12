import { Injectable }  from '@angular/core';
import { HttpClient }  from '@angular/common/http';
import { Observable }  from 'rxjs';
import { environment } from '../../environments/environment';
import { Comment }     from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = `${environment.apiUrl}/comments`;

  constructor(private http: HttpClient) { }

  // Get all comments for an article
  getCommentsByArticle(articleId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/article/${articleId}`);
  }

  // Add a new comment to an article
  addComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.apiUrl, comment);
  }

  // Delete a comment (only allowed for comment owner or article owner)
  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentId}`);
  }
}
