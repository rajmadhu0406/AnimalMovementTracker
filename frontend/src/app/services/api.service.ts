import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api'; // Base API URL

  constructor() { }

  // Generic method for sending API requests
  async request(method: string, endpoint: string, body: any = null, authToken: string | null = null): Promise<any> {
    const url = `${this.baseUrl}/${endpoint}`;
    const headers: HeadersInit = { 'Content-Type': 'application/json' };

    if (authToken) {
      headers['Authorization'] = `Bearer ${authToken}`;
    }

    const options: RequestInit = {
      method,
      headers,
      body: body ? JSON.stringify(body) : null
    };

    try {
      const response = await fetch(url, options);

      if (!response.ok) {
        const errorMessage = await response.text(); // Read text response (if error)
        throw new Error(errorMessage || `HTTP error! Status: ${response.status}`);
      }

      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return await response.json(); // Parse JSON response
      } else {
        return await response.text(); // Return plain text response
      }

      // return response.status !== 204 ? response.json() : null;

    } catch (error: unknown) {
      if (error instanceof Error) {
        throw new Error(error.message);
      }
      throw new Error('Unknown error occurred');
    }
  }
}
