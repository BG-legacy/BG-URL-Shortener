import { Routes } from '@angular/router';
import { AppComponent } from './app.component';

export const routes: Routes = [
  { 
    path: '',           // Default route (empty path)
    component: AppComponent  // Maps to the main AppComponent
  },
  { 
    path: '**',         // Wildcard route - catches all unmatched paths
    redirectTo: ''      // Redirects to the default route
  }
];
