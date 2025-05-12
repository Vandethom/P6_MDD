import { Component, OnInit }                    from '@angular/core';
import { FormBuilder, FormGroup, Validators }   from '@angular/forms';
import { ActivatedRoute, Router }               from '@angular/router';
import { MatSnackBar }                          from '@angular/material/snack-bar';
import { ArticleService, CreateArticleRequest } from '../../services/article.service';
import { AuthService }                          from '../../services/auth.service';
import { ThemeService }                         from '../../services/theme.service';
import { Article }                              from '../../models/article.model';
import { Theme }                                from '../../models/theme.model';
import { catchError, finalize, switchMap, tap } from 'rxjs/operators';
import { EMPTY, Observable }                    from 'rxjs';
import { Location }                             from '@angular/common';

@Component({
  selector: 'app-article-form',
  templateUrl: './article-form.component.html',
  styleUrls: ['./article-form.component.scss']
})
export class ArticleFormComponent implements OnInit {
  articleForm!: FormGroup;
  isEditMode = false;
  articleId?: number;
  isSubmitting = false;
  availableThemes: Theme[] = [];

  constructor(
    private formBuilder   : FormBuilder,
    private articleService: ArticleService,
    private authService   : AuthService,
    private themeService  : ThemeService,
    private router        : Router,
    private route         : ActivatedRoute,
    private snackBar      : MatSnackBar,
    private location      : Location
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadThemes();
    
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.articleId  = +id;
        this.loadArticle(this.articleId);
      }
    });
  }
  
  loadThemes(): void {
    this.themeService.getThemes().subscribe({
      next: (themes: Theme[]) => {
        this.availableThemes = themes;
      },
      error: (error) => {
        console.error('Error loading themes', error);
        this.snackBar.open('Impossible de charger les thèmes', 'Fermer', {
          duration: 5000
        });
      }
    });
  }

  initializeForm(): void {
    this.articleForm = this.formBuilder.group({
      title: ['', [
        Validators.required, 
        Validators.minLength(3)
      ]],
      content: ['', [
        Validators.required,
        Validators.minLength(10)
      ]],
      imageUrl: ['', [
        Validators.pattern('^(https?:\\/\\/)?([\\da-z.-]+)\\.([a-z.]{2,6})([\\/\\w .-]*)*\\/?$')
      ]],
      themeIds: [[]]
    });
  }

  loadArticle(id: number): void {
    this.articleService.getArticle(id).subscribe({
      next: (article: Article) => {
        // Check if current user is the author
        const currentUser = this.authService.getCurrentUser();
        if (!currentUser || currentUser.id !== article.authorId) {
          this.snackBar.open("Vous n'êtes pas autorisé à modifier cet article", "Fermer", {
            duration: 5000
          });
          this.router.navigate(['/']);
          return;
        }
        
        const themeIds = article.themes ? article.themes.map(theme => theme.id) : [];
        
        this.articleForm.patchValue({
          title   : article.title,
          content : article.content,
          imageUrl: article.imageUrl || '',
          themeIds: themeIds
        });
      },
      error: (error) => {
        console.error('Error loading article', error);
        this.snackBar.open("Impossible de charger l'article", "Fermer", {
          duration: 5000
        });
        this.router.navigate(['/']);
      }
    });
  }

  onSubmit(): void {
    if (this.articleForm.invalid) return;
    
    this.isSubmitting = true;
    const formData = this.prepareFormData();
    
    const action$: Observable<Article> = this.isEditMode && this.articleId
      ? this.articleService.updateArticle(this.articleId, formData)
      : this.articleService.createArticle(formData);

    action$.pipe(
      catchError(error => {
        console.error(this.isEditMode ? 'Error updating article' : 'Error creating article', error);
        this.snackBar.open(
          this.isEditMode ? "Erreur lors de la modification de l'article" : "Erreur lors de la création de l'article", 
          "Fermer", 
          { duration: 5000 }
        );
        return EMPTY;
      }),
      finalize(() => {
        this.isSubmitting = false;
      })
    ).subscribe(article => {
      this.snackBar.open(
        this.isEditMode ? "Article modifié avec succès" : "Article créé avec succès", 
        "Fermer", 
        { duration: 3000 }
      );
      this.router.navigate(['/']);
    });
  }

  prepareFormData(): CreateArticleRequest {
    const formValue = this.articleForm.value;
    return {
      title   : formValue.title || '',
      content : formValue.content || '',
      imageUrl: formValue.imageUrl || undefined,
      themeIds: formValue.themeIds || []
    };
  }

  goBack(): void {
    this.location.back();
  }
}
