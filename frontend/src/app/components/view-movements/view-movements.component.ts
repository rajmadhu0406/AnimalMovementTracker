import { Component, OnInit, ElementRef, ViewChild, Inject, PLATFORM_ID } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ErrorService } from '../../services/error.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
// import * as L from 'leaflet';

// Define Farm and Movement interfaces
interface Farm {
  id: number;
  premiseId: string;
  name: string;
  latitude: number;
  longitude: number;
  totalAnimal: number;
  address: string;
  state: string;
  city: string;
  postalCode: string;
}

interface Movement {
  accountCompany: string;
  newMovementReason: string;
  newNumItemsMoved: number;
  newShipmentsStartDate: string;
  newSpecies: string;
  newOriginFarm: Farm;
  newDestinationFarm: Farm;
}

//Dynamically import Leaflet only in the browser to fix SSR issues
declare let L: any;

@Component({
  selector: 'app-view-movements',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-movements.component.html',
  styleUrl: './view-movements.component.scss'
})
export class ViewMovementsComponent implements OnInit {



  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef;
  map!: any;
  authToken: string | null = null;
  farms: Farm[] = [];
  movements: Movement[] = [];
  isBrowser: boolean;

  constructor(
    private apiService: ApiService,
    private errorService: ErrorService,
    @Inject(PLATFORM_ID) private platformId: object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  async ngOnInit(): Promise<void> {
    // Retrieve auth token from local storage
    this.authToken = localStorage.getItem('token');
    if (!this.authToken) {
      // Display error message if no token
      this.errorService.setErrorMessage('Unauthorized: Please log in.');
      return;
    }

    // Ensure Leaflet is only loaded in the browser
    if (this.isBrowser) {
      // Load Leaflet in the browser
      await this.loadLeaflet();
      // Initialize map and fetch data after a short delay
      setTimeout(() => {
        this.initMap();
        this.fetchData();
      }, 500);
    }
  }

  // Dynamically Import Leaflet Only in the Browser
  private async loadLeaflet(): Promise<void> {
    if (typeof window !== 'undefined' && !window.L) {
      const leaflet = await import('leaflet');
      window.L = leaflet;
    }
  }

  // Initialize Leaflet Map
  initMap(): void {
    if (!this.mapContainer?.nativeElement) return;

    // Initialize a Leaflet map and center it on the USA
    this.map = L.map(this.mapContainer.nativeElement).setView([39.8283, -98.5795], 5);

    // Add an OpenStreetMap tile layer
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '  OpenStreetMap contributors'
    }).addTo(this.map);
  }

  // Fetch farms & movements from API
  async fetchData(): Promise<void> {
    try {
      // Fetch farms
      this.farms = await this.apiService.request('GET', 'farm', null, this.authToken);
      // Fetch movements
      this.movements = await this.apiService.request('GET', 'movement', null, this.authToken);
      // Update the map with the fetched data
      this.updateMap();
    } catch (error) {
      // Display error message if data could not be fetched
      this.errorService.setErrorMessage(error instanceof Error ? error.message : 'Failed to load data.');
    }
  }

  // Update map with farm markers & movement paths
  updateMap(): void {

    if (!this.map) {
      console.error("Map is not initialized yet.");
      return;
    }

    const farmMap: { [key: string]: Farm } = {};

    // Add Farm Markers
    this.farms.forEach(farm => {
      // Create a marker with a farm map icon
      const farmMarker = L.marker([farm.latitude, farm.longitude], {
        icon: L.icon({
          iconUrl: 'assets/farm-map-icon.png',
          iconSize: [25, 41],
          iconAnchor: [12, 41],
          popupAnchor: [1, -34]
        })
      })
        // Add a popup with farm details
        .bindPopup(`
          <b>Farm:</b> ${farm.name}<br>
          <b>Premise ID:</b> ${farm.premiseId}<br>
          <b>Location:</b>${farm.address}, ${farm.city}, ${farm.state}, ${farm.postalCode}<br>
          <b>Animals:</b> ${farm.totalAnimal}
        `)
        // Add the marker to the map
        .addTo(this.map); //Ensure `this.map` is defined before using `.addTo()`

      // Store the farm in a map for easy lookups
      farmMap[farm.premiseId] = farm;
    });

    // Draw Movement Paths
    const minWeight = 0.5;  // Minimum line thickness
    const maxWeight = 4; // Maximum line thickness

    //find the max number of animals moved
    const maxItemsMoved = Math.max(...this.movements.map(m => m.newNumItemsMoved), 1); // Prevent division by 0


    this.movements.forEach(movement => {
      const origin = farmMap[movement.newOriginFarm?.premiseId];
      const destination = farmMap[movement.newDestinationFarm?.premiseId];

      // Ensure origin and destination are defined and not the same farm
      if (origin && destination && origin !== destination) {

        // Calculate line thickness based on number of animals moved
        const relativeWeight = minWeight + (movement.newNumItemsMoved / maxItemsMoved) * (maxWeight - minWeight);

        L.polyline(
          [[origin.latitude, origin.longitude], [destination.latitude, destination.longitude]],
          { color: 'blue', weight: relativeWeight, opacity: 0.7 }
        )
          // Add a popup with movement details
          .bindPopup(`
            <b>Movement:</b> ${movement.newNumItemsMoved} animals<br>
            <b>Species:</b> ${movement.newSpecies}<br>
            <b>Company:</b> ${movement.accountCompany}<br>
            <b>Reason:</b> ${movement.newMovementReason}<br>
            <b>Origin:</b> ${origin.name}<br>
            <b>Destination:</b> ${destination.name}<br>
            <b>Date:</b> ${new Date(movement.newShipmentsStartDate).toDateString()}
          `)
          // Add the polyline to the map
          .addTo(this.map); //Fix: Ensure `this.map` is defined
      }
    });
    
  }

}

