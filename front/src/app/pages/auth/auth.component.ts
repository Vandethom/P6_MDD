import { Component, OnInit }                  from '@angular/core';
import { Router, ActivatedRoute }             from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService }                        from '../../services/auth.service';
import { MatDialog }                          from '@angular/material/dialog';
import { MatSnackBar }                        from '@angular/material/snack-bar';
import { PasswordValidator }                  from '../../validators/password.validator';

@Component({
  selector   : 'app-auth',
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
    private route      : ActivatedRoute,
    private fb         : FormBuilder,
    private snackBar   : MatSnackBar
  ) { }

  ngOnInit(): void {
    // Determine if we're in login or register mode based on the route data
    this.route.data.subscribe(data => {
      this.isLoginMode = data['isLogin'] !== false;
      this.initForm();
    });
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
        password: ['', [Validators.required, PasswordValidator.strongPassword()]]
      });
    }
  }
  toggleAuthMode(): void {
    if (this.isLoginMode) {
      this.router.navigate(['/register']);
    } else {
      this.router.navigate(['/login']);
    }
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

  getPasswordErrorMessage(): string {
    const passwordControl = this.authForm.get('password');
    if (passwordControl?.errors) {
      return PasswordValidator.getErrorMessage(passwordControl.errors);
    }
    return '';
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
