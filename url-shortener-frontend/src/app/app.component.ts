import { Component } from '@angular/core';
import { UrlService, UrlResponseDto } from './services/url.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

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
    MatIconModule
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
              <input matInput [value]="shortUrl" readonly>
              <button mat-icon-button matSuffix (click)="copyToClipboard()" [matTooltip]="'Copy to clipboard'">
                <mat-icon>content_copy</mat-icon>
              </button>
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
    }

    .url-card {
      backdrop-filter: blur(10px);
      background: rgba(255, 255, 255, 0.05) !important;
      border: 1px solid rgba(255, 255, 255, 0.1);
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
      margin-top: 1.5rem;
    }

    .error-message {
      color: #ff4444;
      margin-top: 1rem;
      text-align: center;
    }

    .click-count {
      text-align: center;
      margin-top: 1rem;
      color: var(--galaxy-text);
    }

    mat-form-field {
      margin-bottom: 0.5rem;
    }
  `]
})
export class AppComponent {
  url: string = '';
  shortUrl: string = '';
  error: string = '';
  clickCount: number | null = null;

  constructor(private urlService: UrlService) {}

  onSubmit() {
    if (!this.url) return;
    
    if (!this.isValidUrl(this.url)) {
      this.error = 'Please enter a valid URL';
      return;
    }

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
  }

  copyToClipboard() {
    navigator.clipboard.writeText(this.shortUrl).then(() => {
      console.log('URL copied to clipboard');
    });
  }

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

  private isValidUrl(url: string): boolean {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  }
}
