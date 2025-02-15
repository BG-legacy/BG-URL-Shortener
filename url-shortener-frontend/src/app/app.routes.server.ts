import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '**',              // Matches all routes in the application
    renderMode: RenderMode.Prerender  // Specifies that all routes should be pre-rendered during build
  }
];
