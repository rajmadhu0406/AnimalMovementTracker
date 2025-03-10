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
    // Subscribe to the isLoggedIn observable and set the isLoggedIn property
    // and the userRole property accordingly
    this.authService.isLoggedIn().subscribe(status => {
      this.isLoggedIn = status;
      if(status){
        // If the user is logged in, get their role from the payload
        const payload = this.authService.getJwtPayload();
        if(payload){
          this.userRole = payload.role;
        }
        else{
          // If there is no payload, the user role is null
          this.userRole = null;
        }
      }
      else{
        // If the user is not logged in, the user role is null
        this.userRole = null;
      }
    });
  }

  // Clear the JWT payload and log the user out
  logout() {
    this.authService.clearJwtPayload();
    this.authService.logout();
    this.router.navigate(['/']);
  }

  // Check if the user has the required role
  hasPermission(requiredRole: string): boolean {
    return this.authService.hasPermission([requiredRole]);
  }

}
