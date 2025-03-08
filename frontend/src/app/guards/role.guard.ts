// src/app/guards/role.guard.ts
import { Injectable, Inject, forwardRef } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NoPermissionModalComponent } from '../components/no-permission-modal/no-permission-modal.component';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, 
    private router: Router,
    @Inject(forwardRef(() => NgbModal)) private modalService: NgbModal) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const requiredRole = route.data['allowedRoles']; // Get the required role from route data
    const hasPermission = this.authService.hasPermission(requiredRole);

    if (hasPermission) {
      return true; // Allow access
    } else {
      this.modalService.open(NoPermissionModalComponent, { centered: true });
      return false; // Deny access
    }
  }
}