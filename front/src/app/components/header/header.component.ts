import { Component, OnInit } from '@angular/core';
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
}
