import { mergeApplicationConfig, ApplicationConfig } from '@angular/core';
import { provideServerRendering } from '@angular/platform-server';
import { provideServerRouting } from '@angular/ssr';
import { appConfig } from './app.config';
import { serverRoutes } from './app.routes.server';

// Server-specific configuration for Angular Universal (Server-Side Rendering)
const serverConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(),    // Enables server-side rendering capabilities
    provideServerRouting(serverRoutes)  // Configures server-side routing
  ]
};

// Merge the base application config with server-specific config
// This combines browser and server configurations for SSR (Server-Side Rendering)
export const config = mergeApplicationConfig(appConfig, serverConfig);
