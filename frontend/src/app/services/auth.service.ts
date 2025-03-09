// src/app/services/auth.service.ts
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = 'http://localhost:8080/api/auth/login';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  private jwtPayload = new BehaviorSubject<any>(null);

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}


  login(credentials: { username: string; password: string }): Promise<any> {
    return fetch(this.authUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(credentials)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Login failed');
        }
        return response.json();
      })
      .then(data => {
        if (data.token) {
          localStorage.setItem('token', data.token);
          this.loggedIn.next(true);
        }
        return data;
      })
      .catch(error => {
        console.error('Error during login:', error);
        throw error;
      });
  }

  logout(): void {
    localStorage.removeItem('token');
    this.loggedIn.next(false);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  private hasToken(): boolean {
    if (typeof window !== 'undefined' && window.localStorage) {
      return !!localStorage.getItem('token');
    }
    return false;
  }

  private decodeJwtPayload(token: string): any {
    if (!token) {
      return null;
    }
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  }

  // Decode and store the JWT payload
  setJwtPayload(token: string): void {
    const payload = this.decodeJwtPayload(token);
    this.jwtPayload.next(payload);
  }

  // Clear the JWT payload (e.g., on logout)
  clearJwtPayload(): void {
    this.jwtPayload.next(null);
  }

  // Get the current JWT payload
  getJwtPayload(): any {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      if (token) {
        return this.decodeJwtPayload(token); // Decode the JWT payload
      }
    }
    return null;
  }

  hasPermission(allowedRoles: string[]): boolean {
    const payload = this.getJwtPayload();
    return this.isLoggedIn() && allowedRoles.includes(payload?.role);
  }
}
