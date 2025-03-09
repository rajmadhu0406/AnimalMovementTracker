import { Component } from '@angular/core';

@Component({
  selector: 'app-farms',
  imports: [],
  templateUrl: './farms.component.html',
  styleUrl: './farms.component.scss'
})
export class FarmsComponent implements OnInit {
  farms: any[] = []; // Stores fetched farms
  showCreateForm: boolean = false; // Controls visibility of the create farm form
  newFarm = { premiseId: '', totalAnimal: 0 }; // Stores new farm data
  private apiUrl = 'http://localhost:8080/api/farm'; // Base API URL

  constructor() {}

  ngOnInit(): void {
    this.fetchFarms(); // Fetch farms when the component initializes
  }

  // Fetch all farms from the API using Fetch API
  fetchFarms(): void {
    fetch(this.apiUrl)
      .then(response => {
        if (!response.ok) {
          throw new Error(`Error fetching farms: ${response.statusText}`);
        }
        return response.json();
      })
      .then(data => {
        this.farms = data;
      })
      .catch(error => console.error('Fetch error:', error));
  }

  // Delete a farm by ID
  deleteFarm(id: number): void {
    fetch(`${this.apiUrl}/${id}`, {
      method: 'DELETE'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error(`Error deleting farm: ${response.statusText}`);
        }
        // Remove the deleted farm from the local array
        this.farms = this.farms.filter(farm => farm.id !== id);
      })
      .catch(error => console.error('Delete error:', error));
  }

  // Toggle the visibility of the create farm form
  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
  }

  // Create a new farm by sending a POST request
  createFarm(): void {
    fetch(this.apiUrl, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(this.newFarm)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error(`Error creating farm: ${response.statusText}`);
        }
        return response.json();
      })
      .then(createdFarm => {
        this.farms.push(createdFarm); // Add the new farm to the list
        this.newFarm = { premiseId: '', totalAnimal: 0 }; // Reset form
        this.showCreateForm = false; // Hide form after creation
      })
      .catch(error => console.error('Create error:', error));
  }
}

