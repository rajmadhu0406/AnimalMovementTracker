import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, withComponentInputBinding } from '@angular/router'; // Import routing providers
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes'; // Import your routes
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { importProvidersFrom } from '@angular/core';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    importProvidersFrom(NgbModule),
  ]
});