import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ErrorService } from '../../services/error.service';
import * as L from 'leaflet';


@Component({
  selector: 'app-view-movements',
  standalone: true,
  imports: [CommonModule, NgxChartsModule],
  templateUrl: './view-movements.component.html',
  styleUrl: './view-movements.component.scss'
})
export class ViewMovementsComponent implements OnInit {
  private map: L.Map | undefined;
  private farms: any[] = [];
  private movements: any[] = [];
  private authToken: string | null = null; // Authentication token

  constructor(private apiService: ApiService, private authService: AuthService, private errorService: ErrorService) {}

  ngOnInit(): void {
    this.initMap();
    this.fetchData();
  }

  // Initialize the map
  private initMap(): void {
    this.map = L.map('map').setView([0, 0], 2); // Default view

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);
  }

  // Fetch farms and movements data
  private async fetchData(): Promise<void> {
    this.authToken = localStorage.getItem('token'); // Retrieve auth token from local storage
    if (!this.authToken) {
      this.errorService.setErrorMessage('Unauthorized: Please log in to access farm data.');
      return;
    }

    try {
      this.farms = await this.apiService.request('GET', 'farm', null, this.authToken);
      this.movements = await this.apiService.request('GET', 'movement', null, this.authToken);
      this.plotMovements();
    } catch (error) {
      console.error('Failed to fetch data:', error);
    }
  }

  // Plot movements on the map
  private plotMovements(): void {
    if (!this.map) return;

    // Add farm markers
    this.farms.forEach(farm => {
      const marker = L.marker([farm.latitude, farm.longitude]).addTo(this.map!);
      marker.bindPopup(`<b>${farm.name}</b><br>${farm.premiseId}`);
    });

    // Add movement lines
    this.movements.forEach(movement => {
      const originFarm = this.farms.find(farm => farm.premiseId === movement.newOriginFarm.premiseId);
      const destinationFarm = this.farms.find(farm => farm.premiseId === movement.newDestinationFarm.premiseId);

      if (originFarm && destinationFarm) {
        const line = L.polyline(
          [
            [originFarm.latitude, originFarm.longitude],
            [destinationFarm.latitude, destinationFarm.longitude]
          ],
          { color: 'blue' }
        ).addTo(this.map!);

        line.bindPopup(`<b>Movement:</b><br>${movement.newMovementReason}<br>Animals: ${movement.newNumItemsMoved}`);
      }
    });
  }
}
