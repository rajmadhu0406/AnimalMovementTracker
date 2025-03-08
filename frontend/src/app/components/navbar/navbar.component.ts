// src/app/components/navbar/navbar.component.ts
import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  isLoggedIn: boolean = false;
  userRole: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.isLoggedIn().subscribe(status => {
      this.isLoggedIn = status;
      if(status){
        const payload = this.authService.getJwtPayload();
        if(payload){
          this.userRole = payload.role;
        }
        else{
          this.userRole = null;
        }
      }
      else{
        this.userRole = null;
      }
    });
  }

  logout() {
    this.authService.clearJwtPayload();
    this.authService.logout();
    this.router.navigate(['/']);
  }

  hasPermission(requiredRole: string): boolean {
    return this.authService.hasPermission([requiredRole]);
  }

}
