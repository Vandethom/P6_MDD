import { NgModule }               from '@angular/core';
import { RouterModule, Routes }   from '@angular/router';
import { HomeComponent }          from './pages/home/home.component';
import { AuthComponent }          from './pages/auth/auth.component';
import { AuthGuard }              from './guards/auth.guard';
import { ArticleFormComponent }   from './components/article-form/article-form.component';
import { UserProfileComponent }   from './pages/user-profile/user-profile.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { ThemesComponent }        from './pages/themes/themes.component';

const routes: Routes = [
  { 
    path     : '', 
    component: HomeComponent
  },
  { 
    path     : 'login', 
    component: AuthComponent,
    data     : { isLogin: true } 
  },
  { 
    path: 'register', 
    component: AuthComponent,
    data: { isLogin: false } 
  },
  { 
    path       : 'home', 
    component  : HomeComponent,
    canActivate: [AuthGuard]
  },
  { 
    path       : 'articles', 
    component  : HomeComponent,
    canActivate: [AuthGuard]
  },  { 
    path       : 'themes', 
    component  : ThemesComponent,
    canActivate: [AuthGuard]
  },{ 
    path       : 'article/:id', 
    component  : ArticleDetailComponent,
    canActivate: [AuthGuard]
  },
  { 
    path       : 'create-article', 
    component  : ArticleFormComponent,
    canActivate: [AuthGuard]
  },  { 
    path       : 'edit-article/:id', 
    component  : ArticleFormComponent,
    canActivate: [AuthGuard]
  },
  {
    path       : 'user/:id',
    component  : UserProfileComponent,
    canActivate: [AuthGuard]
  },
  { 
    path      : '**', 
    redirectTo: '' 
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
