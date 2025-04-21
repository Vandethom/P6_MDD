import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent }        from './pages/home/home.component';
import { AuthComponent }        from './pages/auth/auth.component';
import { AuthGuard }            from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: AuthComponent },
  { 
    path       : 'home', 
    component  : HomeComponent,
    canActivate: [AuthGuard]  // Protects the home route with AuthGuard
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})

export class AppRoutingModule {}
