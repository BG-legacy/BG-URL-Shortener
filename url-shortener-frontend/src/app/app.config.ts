import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

// Main application configuration
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),       // Sets up application routing
    provideAnimations(),         // Enables Angular Material animations
    provideHttpClient(           // Configures HTTP client for API calls
      // Add any HTTP interceptors if needed
      // withInterceptors([...])
    )
  ]
};
