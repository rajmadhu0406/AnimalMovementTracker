import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ErrorService } from '../../services/error.service';
import { Movement } from '../../models/movement.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-movements',
  imports: [FormsModule, CommonModule],
  templateUrl: './movements.component.html',
  styleUrl: './movements.component.scss'
})
export class MovementsComponent implements OnInit {
  movements: Movement[] = []; // Stores fetched movements
  showCreateForm = false; // Controls form visibility
  newMovement: Movement = this.getEmptyMovement();
  authToken: string | null = null; // Replace with actual token retrieval

  constructor(private apiService: ApiService, private errorService: ErrorService) {}

  ngOnInit(): void {
    this.authToken = localStorage.getItem('token');
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
  async deleteMovement(id: number): Promise<void> {
    if (!confirm('Are you sure you want to delete this movement?')) return;

    try {
      await this.apiService.request('DELETE', `movement/${id}`, null, this.authToken);
      this.movements = this.movements.filter(movement => movement.id !== id); // Update table
      this.errorService.setErrorMessage('Movement deleted successfully.');
    } catch (error) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to delete movement.');
    }
  }

  // Create a movement
  async createMovement(): Promise<void> {
    try {
      const createdMovement = await this.apiService.request('POST', 'movement', this.newMovement, this.authToken);
      this.movements.push(createdMovement); // Add new movement to the table
      this.newMovement = this.getEmptyMovement(); // Reset form fields
      this.showCreateForm = false; // Hide form after creation
    } catch (error) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to create movement.');
    }
  }

  // Get an empty Movement object
  getEmptyMovement(): Movement {
    return {
      accountCompany: '',
      newMovementReason: '',
      newSpecies: '',
      newOriginAddress: '',
      newOriginCity: '',
      newOriginName: '',
      newOriginPostalCode: '',
      newOriginPremId: '',
      newOriginState: '',
      newDestinationAddress: '',
      newDestinationCity: '',
      newDestinationName: '',
      newDestinationPostalCode: '',
      newDestinationPremId: '',
      newDestinationState: '',
      originLat: 0.1,
      originLon: 0.1,
      destinationLat: 0.1,
      destinationLong: 0.1,
      newNumItemsMoved: 0,
      newShipmentsStartDate: new Date().toISOString()
    };
  }

  // Toggle form visibility
  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
  }
}
