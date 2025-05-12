import { CanActivate, 
  CanLoad, 
  Route, 
  UrlSegment, 
  ActivatedRouteSnapshot, 
  RouterStateSnapshot, 
  UrlTree, 
  Router }             from '@angular/router';
import { Injectable }  from '@angular/core';
import { Observable }  from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {
  
  constructor(
    private authService: AuthService, 
    private router     : Router
  ) {}
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
    const isAuthPage = route.data && route.data['authPage'] === true;
    const isLoggedIn = this.authService.isLoggedIn();
    
    console.log('AuthGuard: Route path =',   state.url);
    console.log('AuthGuard: Is Auth Page =', isAuthPage);
    console.log('AuthGuard: Is Logged In =', isLoggedIn);
    
    // If this is auth page and user is logged in, redirect to home
    if (isAuthPage && isLoggedIn) {
      console.log('AuthGuard: Redirecting to /home (logged in user on auth page)');
      return this.router.createUrlTree(['/home']);
    }
    
    // If this is not auth page and user is not logged in, redirect to login
    if (!isAuthPage && !isLoggedIn) {
      console.log('AuthGuard: Redirecting to / (not logged in user on protected page)');
      return this.router.createUrlTree(['/']);
    }
    
    // Otherwise allow navigation
    console.log('AuthGuard: Allowing navigation');
    return true;
  }
  
  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
    const isLoggedIn = this.authService.isLoggedIn();
    console.log('AuthGuard canLoad: Is Logged In =', isLoggedIn);
    
    if (!isLoggedIn) {
      console.log('AuthGuard canLoad: Redirecting to / (not logged in)');
      return this.router.createUrlTree(['/']);
    }
    
    console.log('AuthGuard canLoad: Allowing navigation');
    return true;
  }
}