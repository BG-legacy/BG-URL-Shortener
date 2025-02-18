import { Component } from '@angular/core';
import { UrlService, UrlResponseDto } from './services/url.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatIconModule,
    MatTooltipModule
  ],
  template: `
    <div class="container">
      <h1>Stellar URL Shortener</h1>
      
      <mat-card class="url-card">
        <mat-card-content>
          <mat-form-field class="full-width">
            <mat-label>Enter your URL</mat-label>
            <input matInput [(ngModel)]="url" placeholder="https://example.com">
            <mat-error *ngIf="error">{{error}}</mat-error>
          </mat-form-field>

          <div class="button-container">
            <button mat-raised-button color="primary" (click)="onSubmit()" [disabled]="!url">
              Shorten URL
            </button>
          </div>

          <div *ngIf="shortUrl" class="result-container">
            <mat-form-field class="full-width">
              <mat-label>Shortened URL</mat-label>
              <input matInput [value]="getDisplayUrl(shortUrl)" readonly>
              <div class="url-actions">
                <button mat-icon-button (click)="copyToClipboard()" [matTooltip]="'Copy to clipboard'">
                  <mat-icon>content_copy</mat-icon>
                </button>
                <a [href]="shortUrl" target="_blank" class="short-url-link" (click)="handleUrlClick($event)">
                  <mat-icon>open_in_new</mat-icon>
                </a>
              </div>
            </mat-form-field>

            <div class="click-count" *ngIf="clickCount !== null">
              Clicks: {{clickCount}}
            </div>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .container {
      max-width: 600px;
      margin: 2rem auto;
      padding: 0 1rem;
      position: relative;
      z-index: 1;
    }

    h1 {
      text-align: center;
      margin-bottom: 2rem;
      font-size: 2.5rem;
      background: linear-gradient(45deg, var(--galaxy-primary), var(--galaxy-accent));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      word-wrap: break-word;
    }

    .url-card {
      backdrop-filter: blur(10px);
      background: rgba(255, 255, 255, 0.05);
      border-radius: 12px;
      padding: 1.5rem;
    }

    .full-width {
      width: 100%;
    }

    .button-container {
      display: flex;
      justify-content: center;
      margin: 1rem 0;
    }

    .result-container {
      margin-top: 1rem;
      position: relative;
    }

    .error-message {
      color: #ff4444;
      margin-top: 1rem;
      text-align: center;
    }

    .click-count {
      text-align: center;
      margin-top: 0.5rem;
      color: rgba(255, 255, 255, 0.7);
    }

    .url-actions {
      display: flex;
      gap: 8px;
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%);
      padding-right: 8px;
    }

    .short-url-link {
      color: var(--galaxy-accent);
      text-decoration: none;
      display: flex;
      align-items: center;
      padding: 4px;
      border-radius: 4px;
      transition: background-color 0.2s;

      &:hover {
        background-color: rgba(255, 255, 255, 0.1);
      }

      mat-icon {
        font-size: 20px;
        width: 20px;
        height: 20px;
      }
    }

    mat-form-field {
      margin-bottom: 0.5rem;
    }

    /* Mobile-specific styles */
    @media screen and (max-width: 600px) {
      .container {
        margin: 1rem;
      }

      h1 {
        font-size: 2rem;
      }

      .url-card {
        padding: 1rem;
      }

      .url-actions {
        gap: 4px;
      }

      .mat-icon {
        font-size: 18px;
        width: 18px;
        height: 18px;
      }
    }
  `]
})
export class AppComponent {
  // Main component properties
  title = 'url-shortener-frontend';
  url: string = '';              // Stores the input URL
  shortUrl: string = '';         // Stores the shortened URL
  error: string = '';           // Stores error messages
  clickCount: number | null = null;  // Tracks number of clicks on shortened URL
  loading = false;

  constructor(private urlService: UrlService) {}

  /**
   * Handles the URL shortening submission
   * Validates the URL and calls the URL shortening service
   */
  async onSubmit() {
    if (!this.url) return;
    
    if (!this.isValidUrl(this.url)) {
      this.error = 'Please enter a valid URL';
      return;
    }

    this.loading = true;
    try {
      this.urlService.shortenUrl(this.url).subscribe({
        next: (response: UrlResponseDto) => {
          this.shortUrl = response.shortUrl;
          this.error = '';
          this.trackClick();
        },
        error: (error) => {
          console.error('Error shortening URL:', error);
          this.error = 'Error shortening URL. Please try again.';
        }
      });
    } catch (error) {
      console.error('Error:', error);
      this.error = 'Error shortening URL. Please try again.';
    } finally {
      this.loading = false;
    }
  }

  /**
   * Formats the shortened URL for display
   * Extracts the short ID and prepends the domain
   */
  getDisplayUrl(url: string): string {
    const shortId = url.split('/').pop();
    return `stellar.url/${shortId}`;
  }

  /**
   * Copies the shortened URL to clipboard
   */
  copyToClipboard() {
    navigator.clipboard.writeText(this.shortUrl).then(() => {
      console.log('URL copied to clipboard');
    });
  }

  /**
   * Fetches and updates the click statistics for the shortened URL
   */
  trackClick() {
    if (this.shortUrl) {
      const shortId = this.shortUrl.split('/').pop();
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

  /**
   * Handles clicks on the shortened URL
   * Updates click tracking while allowing default link behavior
   */
  handleUrlClick(event: MouseEvent) {
    // Don't prevent default - let the link open in new tab
    this.trackClick();
  }

  /**
   * Validates if a string is a proper URL
   * @param url - The URL string to validate
   * @returns boolean indicating if the URL is valid
   */
  private isValidUrl(url: string): boolean {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  }
}
