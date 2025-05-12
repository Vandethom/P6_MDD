import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
}                      from '@angular/common/http';
import { Injectable }  from '@angular/core';
import { Observable }  from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.authService.getToken();
    
    if (token) {
      console.log(`Adding auth token to request: ${request.url}`);
      const clonedRequest = request.clone({
        headers: request.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(clonedRequest);
    } else {
      console.log(`No token available for request: ${request.url}`);
    }
    
    return next.handle(request);
  }
}
