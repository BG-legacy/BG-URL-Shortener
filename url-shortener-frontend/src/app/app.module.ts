import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent,    // Declares the root component of the application
  ],
  imports: [
    BrowserModule,           // Essential browser-specific services
    HttpClientModule,        // For making HTTP requests
    FormsModule,            // For template-driven forms
    BrowserAnimationsModule, // Required for Material animations
    MatCardModule,          // Material UI card component
    MatFormFieldModule,     // Material UI form field wrapper
    MatInputModule,         // Material UI input component
    MatButtonModule,        // Material UI button component
    MatIconModule,          // Material UI icons
    MatSnackBarModule,      // Material UI notifications
    ReactiveFormsModule     // For reactive forms
  ],
  providers: [],            // Services would go here if needed
  bootstrap: [AppComponent] // Specifies the root component to bootstrap
})
export class AppModule { } 