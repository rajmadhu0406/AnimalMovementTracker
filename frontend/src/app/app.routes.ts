import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { MovementsComponent } from './components/movements/movements.component';
import { FarmsComponent } from './components/farms/farms.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { ViewMovementsComponent } from './components/view-movements/view-movements.component';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';

// export const routes: Routes = [
//   { path: '', component: HomeComponent },
//   { path: 'login', component: LoginComponent },
//   { path: 'movements', component: MovementsComponent, canActivate: [AuthGuard] },
//   { path: 'farms', component: FarmsComponent, canActivate: [AuthGuard] },
//   { path: 'create-user', component: CreateUserComponent, canActivate: [AuthGuard] },
//   { path: 'view-movements', component: ViewMovementsComponent, canActivate: [AuthGuard] },
//   { path: '**', redirectTo: '' } // Wildcard route for unknown paths
// ];

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { 
    path: 'movements', 
    component: MovementsComponent, 
    canActivate: [RoleGuard], 
    data: { allowedRoles: [ 'USER', 'VIEWER'] } // ADMIN and USER and VIEWER can access
  },
  { 
    path: 'farms', 
    component: FarmsComponent, 
    canActivate: [RoleGuard], 
    data: { allowedRoles: ['ADMIN'] } // ADMIN and USER and VIEWER can access
  },
  { 
    path: 'create-user', 
    component: CreateUserComponent, 
    canActivate: [RoleGuard], 
    data: { allowedRoles: ['ADMIN'] } // Only ADMIN can access
  },
  { 
    path: 'view-movements', 
    component: ViewMovementsComponent, 
    canActivate: [RoleGuard], 
    data: { allowedRoles: ['ADMIN', 'USER', 'VIEWER'] } // ADMIN and USER and VIEWER can access
  },
  { path: '**', redirectTo: '' } // Wildcard route for unknown paths
];

