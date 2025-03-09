import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {
  private errorMessageSubject = new BehaviorSubject<string | null>(null);
  errorMessage$ = this.errorMessageSubject.asObservable();

  setErrorMessage(message: string): void {
    this.errorMessageSubject.next(message);
    setTimeout(() => this.clearErrorMessage(), 5000); // Auto-clear after 5 seconds
  }

  clearErrorMessage(): void {
    this.errorMessageSubject.next(null);
  }
}
