import { Component, OnInit }                  from '@angular/core';
import { Router }                             from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService }                        from '../../services/auth.service';
import { MatDialog }                          from '@angular/material/dialog';
import { MatSnackBar }                        from '@angular/material/snack-bar';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls  : ['./auth.component.scss']
})
export class AuthComponent implements OnInit {
  isLoginMode         = true;
  authForm: FormGroup = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });
  loading = false;

  constructor(
    private authService: AuthService,
    private router     : Router,
    private fb         : FormBuilder,
    private snackBar   : MatSnackBar
  ) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    if (this.isLoginMode) {
      this.authForm = this.fb.group({
        username: ['', [Validators.required]],
        password: ['', [Validators.required, Validators.minLength(6)]]
      });
    } else {
      this.authForm = this.fb.group({
        username: ['', [Validators.required]],
        email   : ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]]
      });
    }
  }

  toggleAuthMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.initForm();
  }

  onSubmit(): void {
    if (this.authForm.invalid) {
      return;
    }

    this.loading = true;

    if (this.isLoginMode) {
      this.login();
    } else {
      this.register();
    }
  }

  login(): void {
    const { username, password } = this.authForm.value;
    this.authService.login(username, password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/home']);
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open('Échec de la connexion: ' + (error.error?.message || 'Erreur inconnue'), 'Fermer', {
          duration: 5000
        });
      }
    });
  }

  register(): void {
    const { username, email, password } = this.authForm.value;
    this.authService.register(
      username, 
      password, 
      email).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/home']);
        },
        error: (error) => {
          this.loading = false;
          this.snackBar.open('Échec de l\'inscription: ' + (error.error?.message || 'Erreur inconnue'), 'Fermer', {
            duration: 5000
          });
        }
    });
  }
}
