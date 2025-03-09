import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ErrorService } from '../../services/error.service';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-farms',
  imports: [FormsModule, CommonModule],
  templateUrl: './farms.component.html',
  styleUrl: './farms.component.scss'
})
export class FarmsComponent implements OnInit {
  farms: any[] = []; // Stores fetched farms
  showCreateForm: boolean = false; // Controls visibility of the create farm form
  newFarm = this.getEmptyFarm(); // Stores new farm data
  authToken: string | null = null; // Authentication token

  constructor(private apiService: ApiService, public errorService: ErrorService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authToken = localStorage.getItem('token'); // Retrieve auth token from local storage
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: Please log in to access farm data.');
      return;
    }
    this.fetchFarms(); // Fetch farms when the component initializes
  }

  // Fetch all farms
  async fetchFarms(): Promise<void> {
    try {
      this.farms = await this.apiService.request('GET', 'farm', null, this.authToken);
    } catch (error: unknown) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to load farms.');
    }
  }

  // Delete a farm by ID
  async deleteFarm(id: number): Promise<void> {
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: You must be logged in to delete farms.');
      return;
    }
    if (!confirm('Are you sure you want to delete this farm?')) return;

    try {
      await this.apiService.request('DELETE', `farm/${id}`, null, this.authToken);
      this.farms = this.farms.filter(farm => farm.id !== id);
      this.errorService.setErrorMessage('Farm deleted successfully.');
    } catch (error: unknown) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to delete farm.');
    }
  }

  // Toggle the visibility of the create farm form
  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
  }

  // Create a farm
  async createFarm(): Promise<void> {
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: You must be logged in to create farms.');
      return;
    }

    // Validate inputs
    if (!this.newFarm.premiseId) {
      this.errorService.setErrorMessage('Invalid input: Premise ID is required.');
      return;
    }
    if (this.newFarm.totalAnimal < 0) {
      this.errorService.setErrorMessage('Invalid input: Total animals must be non-negative.');
      return;
    }
    if (!this.newFarm.latitude || !this.newFarm.longitude) {
      this.errorService.setErrorMessage('Invalid input: Latitude and longitude are required.');
      return;
    }
    if (!this.newFarm.address || !this.newFarm.state || !this.newFarm.city || !this.newFarm.name || !this.newFarm.postalCode) {
      this.errorService.setErrorMessage('Invalid input: Address, state, city, name, and postal code are required.');
      return;
    }

    try {
      const createdFarm = await this.apiService.request('POST', 'farm', this.newFarm, this.authToken);
      this.farms.push(createdFarm);
      this.newFarm = this.getEmptyFarm(); // Reset form
      this.showCreateForm = false; // Hide form after creation
      this.errorService.setErrorMessage('Farm created successfully.');
    } catch (error: unknown) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to create farm.');
    }
  }

  // Get an empty farm object for resetting the form
  getEmptyFarm() {
    return {
      premiseId: '',
      totalAnimal: 0,
      latitude: 0,
      longitude: 0,
      address: '',
      state: '',
      city: '',
      name: '',
      postalCode: ''
    };
  }
}
