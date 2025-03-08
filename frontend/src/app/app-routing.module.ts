// // src/app/app-routing.module.ts
// import { NgModule } from '@angular/core';
// import { RouterModule, Routes } from '@angular/router';
// import { LoginComponent } from './components/login/login.component';
// import { HomeComponent } from './components/home/home.component';
// import { MainComponent } from './main/main.component';

// const routes: Routes = [
//   { 
//     path: '', 
//     component: MainComponent,
//     children: [
//       { path: '', component: HomeComponent }, // Home route
//       { path: 'login', component: LoginComponent } // Login route
//     ]
//   }
// ];

// @NgModule({
//   imports: [RouterModule.forRoot(routes)],
//   exports: [RouterModule]
// })
// export class AppRoutingModule { }