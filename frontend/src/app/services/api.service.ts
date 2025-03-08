// api.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor() {}

  // Generic fetch method to handle different HTTP methods
  async fetchData(
    url: string,
    method: string = 'GET',
    body: any = null,
    headers: { [key: string]: string } = {}
  ): Promise<any> {
    try {
      const options: RequestInit = {
        method,
        headers: {
          'Content-Type': 'application/json',
          ...headers, // Add custom headers (e.g., Bearer token)
        },
      };

      // Add body for POST/PUT requests
      if (body && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(body);
      }

      const response = await fetch(url, options);

      // Check if the response is OK (status 200-299)
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'An error occurred');
      }

      return await response.json(); // Parse and return the response data
    } catch (error) {
      throw error; // Re-throw the error for handling in components
    }
  }

  // Convenience methods for different HTTP methods
  get(url: string, headers: { [key: string]: string } = {}): Promise<any> {
    return this.fetchData(url, 'GET', null, headers);
  }

  post(
    url: string,
    body: any,
    headers: { [key: string]: string } = {}
  ): Promise<any> {
    return this.fetchData(url, 'POST', body, headers);
  }

  put(
    url: string,
    body: any,
    headers: { [key: string]: string } = {}
  ): Promise<any> {
    return this.fetchData(url, 'PUT', body, headers);
  }

  delete(url: string, headers: { [key: string]: string } = {}): Promise<any> {
    return this.fetchData(url, 'DELETE', null, headers);
  }
}