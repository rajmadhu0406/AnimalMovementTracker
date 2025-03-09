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
  newFarm = { premiseId: '', totalAnimal: 0 }; // Stores new farm data
  authToken: string | null = null; // Replace with actual token retrieval

  constructor(private apiService: ApiService, public errorService: ErrorService, private authService: AuthService) { }

  ngOnInit(): void {
    this.authToken = localStorage.getItem('token');
    this.fetchFarms(); // Fetch farms when the component initializes
  }
  // Fetch all farms
  async fetchFarms(): Promise<void> {
    try {
      this.farms = await this.apiService.request('GET', 'farm', null, this.authToken);
    } catch (error: unknown) {
      if (error instanceof Error) {
        this.errorService.setErrorMessage(error.message);
      } else {
        this.errorService.setErrorMessage('An unknown error occurred');
      }
    }
  }

  // Delete a farm by ID
  async deleteFarm(id: number): Promise<void> {
    if (!confirm('Are you sure you want to delete this farm?')) return;
  
    try {
      // Call API service to send DELETE request
      await this.apiService.request('DELETE', `farm/${id}`, null, this.authToken);
  
      // pdate UI only if delete was successful
      this.farms = this.farms.filter(farm => farm.id !== id);
  
      // Show success message
      this.errorService.setErrorMessage('Farm deleted successfully.');
    } catch (error: unknown) {
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'An unknown error occurred.');
    }
  }
  



  // Toggle the visibility of the create farm form
  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
  }

  // Create a farm
  async createFarm(): Promise<void> {
    if (!this.newFarm.premiseId) {
      this.errorService.setErrorMessage('Invalid input: Premise ID is required.');
      return;
    }

    if (this.newFarm.totalAnimal < 0) {
      this.errorService.setErrorMessage('Invalid input: Total animals must be non-negative.');
      return;
    }


    try {
      const createdFarm = await this.apiService.request('POST', 'farm', this.newFarm, this.authToken);
      this.farms.push(createdFarm);
      this.newFarm = { premiseId: '', totalAnimal: 0 }; // Reset form
      this.showCreateForm = false; // Hide form after creation
    } catch (error: unknown) {
      if (error instanceof Error) {
        this.errorService.setErrorMessage(error.message);
      } else {
        this.errorService.setErrorMessage('An unknown error occurred');
      }
    }
  }


}

