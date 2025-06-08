import { NgModule }                         from '@angular/core';
import { BrowserModule }                    from '@angular/platform-browser';
import { BrowserAnimationsModule }          from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';

import { MatButtonModule }          from '@angular/material/button';
import { MatCardModule }            from '@angular/material/card';
import { MatChipsModule }           from '@angular/material/chips';
import { MatDialogModule }          from '@angular/material/dialog';
import { MatFormFieldModule }       from '@angular/material/form-field';
import { MatIconModule }            from '@angular/material/icon';
import { MatInputModule }           from '@angular/material/input';
import { MatMenuModule }            from '@angular/material/menu';
import { MatSelectModule }          from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule }        from '@angular/material/snack-bar';

import { AppComponent }    from './app.component';
import { HomeComponent }   from './pages/home/home.component';
import { AuthComponent }   from './pages/auth/auth.component';
import { HeaderComponent } from './components/header/header.component';

import { AuthService }    from './services/auth.service';
import { ArticleService } from './services/article.service';
import { ThemeService }   from './services/theme.service';

import { AppRoutingModule } from './app-routing.module';
import { ArticleFormComponent } from './components/article-form/article-form.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { ThemeDetailComponent } from './pages/theme-detail/theme-detail.component';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AuthComponent,
    HeaderComponent,
    ArticleFormComponent,
    UserProfileComponent,
    ArticleDetailComponent,
    ThemesComponent,
    ThemeDetailComponent,
    ConfirmationDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
      // Angular Material
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],  providers: [
    AuthService,
    ArticleService,
    ThemeService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
