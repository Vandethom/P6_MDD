import { Component, OnInit, HostListener } from '@angular/core';
import { Router }            from '@angular/router';
import { AuthService }       from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls  : ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  username: string = '';
  userId: number = 0;
  isMobileMenuOpen: boolean = false;

  constructor(
    private authService: AuthService,
    private router     : Router
  ) { }

  ngOnInit(): void {
    this.username = this.authService.getUsername() || 'Utilisateur';
    const user    = this.authService.getCurrentUser();
    if (user) {
      this.userId = user.id;
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  toggleMobileMenu(): void {
    console.log('Toggle menu clicked, current state:', this.isMobileMenuOpen);
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    console.log('New state:', this.isMobileMenuOpen);
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
  }

  @HostListener('document:keydown.escape', ['$event'])
  onEscapePress(event: KeyboardEvent): void {
    if (this.isMobileMenuOpen) {
      this.closeMobileMenu();
    }
  }
}
