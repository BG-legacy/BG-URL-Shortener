import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { config } from './app/app.config.server';

// Function to bootstrap the application in server-side rendering mode
const bootstrap = () => bootstrapApplication(AppComponent, config);

// Export the bootstrap function for use in server-side rendering
export default bootstrap;
