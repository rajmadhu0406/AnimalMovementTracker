import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ErrorService } from '../../services/error.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-user',
  imports: [FormsModule, CommonModule],
  templateUrl: './create-user.component.html',
  styleUrl: './create-user.component.scss'
})
export class CreateUserComponent {
  newUser = this.getEmptyUser();
  authToken: string | null = null;

  constructor(private apiService: ApiService, private errorService: ErrorService) {}

  ngOnInit(): void {
    this.authToken = localStorage.getItem('token');
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: Please log in to create a user.');
      return;
    }
  }

  // Create User
  async createUser(): Promise<void> {
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: You must be logged in to create users.');
      return;
    }

    // Validate password length
    if (this.newUser.password.length < 8) {
      this.errorService.setErrorMessage('Password must be at least 8 characters long.');
      return;
    }

    try {
      console.info(this.newUser);
      const createdUser = await this.apiService.request('POST', 'users', this.newUser, this.authToken);
      this.errorService.setErrorMessage('User created successfully.');
      this.newUser = this.getEmptyUser(); // Reset form
    } catch (error) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to create user.');
    }
  }

  // Get an empty user object for resetting the form
  getEmptyUser() {
    return {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      password: '',
      role: {roleType: 'USER'}
    };
  }
}
