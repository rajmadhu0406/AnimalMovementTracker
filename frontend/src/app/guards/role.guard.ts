// src/app/guards/role.guard.ts
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NoPermissionModalComponent } from '../components/no-permission-modal/no-permission-modal.component';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router, private modalService: NgbModal, @Inject(PLATFORM_ID) private platformId: Object) {}
  // Check if the user has the required role
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const requiredRole = route.data['allowedRoles']; // Get the required role from route data
    const hasPermission = this.authService.hasPermission(requiredRole);

    // If the user has the required role, allow access
    if (hasPermission) {
      return true; // Allow access
    } else {

      // If the user does not have the required role, show the "Access Denied" modal
      if (isPlatformBrowser(this.platformId)) {
        this.modalService.open(NoPermissionModalComponent, { centered: true });// Open modal in browser only
      }
      this.router.navigate(['/']); // Redirect to home page
      return false;
    }
  }
}