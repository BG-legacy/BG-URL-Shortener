import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { UrlService, UrlResponseDto } from './services/url.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,      // For common Angular directives
    FormsModule,       // For form handling
    MatInputModule,    // Material UI input component
    MatButtonModule,   // Material UI button component
    MatCardModule,     // Material UI card component
    MatFormFieldModule,// Material UI form field wrapper
    HttpClientModule,  // For HTTP requests
    MatIconModule      // Material UI icons
  ],
  template: `
    <!-- Main container with galaxy theme -->
    <div class="galaxy-container">
      <div class="content-wrapper">
        <h1 class="title">Stellar URL Shortener</h1>
        <div class="container">
          <!-- URL submission form -->
          <form #urlForm="ngForm" (ngSubmit)="onSubmit()">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Enter your URL</mat-label>
              <input matInput [(ngModel)]="url" name="url" required placeholder="https://example.com">
            </mat-form-field>
            <button mat-raised-button color="primary" type="submit" [disabled]="!urlForm.form.valid">
              Shorten URL
            </button>
          </form>

          <!-- Results card showing shortened URL -->
          <div *ngIf="shortUrl" class="result-container">
            <mat-card>
              <mat-card-content>
                <p>Your shortened URL:</p>
                <p class="short-url">
                  <a [href]="shortUrl" target="_blank" (click)="trackClick()">{{ shortUrl }}</a>
                </p>
                <p *ngIf="clickCount !== null" class="click-count">
                  Clicks: {{ clickCount }}
                </p>
                <button mat-button color="primary" (click)="copyToClipboard()">
                  <mat-icon>content_copy</mat-icon>
                  Copy to Clipboard
                </button>
              </mat-card-content>
            </mat-card>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .galaxy-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
    }

    .content-wrapper {
      max-width: 600px;
      width: 100%;
      text-align: center;
    }

    .title {
      font-size: 2.5rem;
      margin-bottom: 2rem;
      font-weight: 300;
      color: var(--galaxy-text);
      text-shadow: 0 0 10px rgba(110, 59, 156, 0.5);
    }

    form {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }

    .result {
      margin-top: 2rem;
    }

    mat-card {
      margin-top: 1rem;
    }

    a {
      color: var(--galaxy-accent);
      text-decoration: none;
      display: block;
      margin: 1rem 0;
      word-break: break-all;
      
      &:hover {
        text-decoration: underline;
      }
    }

    button {
      margin: 0.5rem;
    }

    .short-url {
      font-size: 1.2rem;
      word-break: break-all;
      margin: 0.5rem 0;
      padding: 0.5rem;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 4px;

      a {
        color: var(--galaxy-accent);
        text-decoration: none;
        
        &:hover {
          text-decoration: underline;
        }
      }
    }

    .click-count {
      font-size: 1.1rem;
      color: var(--galaxy-text);
      margin: 0.5rem 0;
    }
  `]
})
export class AppComponent {
  title = 'url-shortener-frontend';  // Application title
  url: string = '';                  // Stores the input URL
  shortUrl: string = '';            // Stores the shortened URL
  error: string = '';               // Stores error messages
  clickCount: number | null = null; // Tracks number of clicks on shortened URL

  constructor(private urlService: UrlService) {}

  // Handles form submission
  onSubmit() {
    if (!this.url) return;
    
    this.urlService.shortenUrl({ url: this.url }).subscribe({
      next: (response: UrlResponseDto) => {
        this.shortUrl = response.shortUrl;
        this.error = '';
      },
      error: (error) => {
        console.error('Error shortening URL:', error);
        this.error = 'Error shortening URL. Please try again.';
      }
    });
  }

  // Copies shortened URL to clipboard
  copyToClipboard() {
    navigator.clipboard.writeText(this.shortUrl).then(() => {
      console.log('URL copied to clipboard');
    });
  }

  // Tracks clicks on the shortened URL
  trackClick() {
    if (this.shortUrl) {
      const shortId = this.shortUrl.split('/').pop(); // Extract the ID from the URL
      if (shortId) {
        this.urlService.getUrlStats(shortId).subscribe({
          next: (response: UrlResponseDto) => {
            this.clickCount = response.clickCount;
          },
          error: (error) => {
            console.error('Error tracking click:', error);
          }
        });
      }
    }
  }
}
