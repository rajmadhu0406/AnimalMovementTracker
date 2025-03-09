import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ErrorService } from '../../services/error.service';
import { Movement } from '../../models/movement.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-movements',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './movements.component.html',
  styleUrl: './movements.component.scss'
})
export class MovementsComponent implements OnInit {
  movements: any[] = []; // Stores fetched movements
  showCreateForm = false; // Controls form visibility
  newMovement = this.getEmptyMovement();
  authToken: string | null = null; // Authentication token

  constructor(private apiService: ApiService, private errorService: ErrorService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authToken = localStorage.getItem('token');
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: Please log in to access movement data.');
      return;
    }
    this.fetchMovements();
  }

  // Fetch all movements
  async fetchMovements(): Promise<void> {
    try {
      this.movements = await this.apiService.request('GET', 'movement', null, this.authToken);
    } catch (error) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to load movements.');
    }
  }

  // Delete a movement
  async deleteMovement(movement: any): Promise<void> {
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: You must be logged in to delete movements.');
      return;
    }
    if (!confirm('Are you sure you want to delete this movement?')) return;

    try {
      await this.apiService.request('DELETE', `movement/${movement.newOriginFarm.premiseId}/${movement.newDestinationFarm.premiseId}`, null, this.authToken);
      this.movements = this.movements.filter(m => m !== movement);
      this.errorService.setErrorMessage('Movement deleted successfully.');
    } catch (error) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to delete movement.');
    }
  }

  //Create a movement
  async createMovement(): Promise<void> {
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: You must be logged in to create movements.');
      return;
    }

    try {
      const createdMovement = await this.apiService.request('POST', 'movement', this.newMovement, this.authToken);
      this.movements.push(createdMovement);
      this.newMovement = this.getEmptyMovement(); // Reset form
      this.showCreateForm = false;
      this.errorService.setErrorMessage('Movement created successfully.');
    } catch (error) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to create movement.');
    }
  }

  //Get an empty movement object for resetting the form
  getEmptyMovement() {
    return {
      accountCompany: '',
      newMovementReason: '',
      newSpecies: '',
      newNumItemsMoved: 0,
      newShipmentsStartDate: new Date().toISOString(),
      newOriginFarm: '',
      newDestinationFarm: ''
    };
  }

  // Toggle form visibility
  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
  }
}