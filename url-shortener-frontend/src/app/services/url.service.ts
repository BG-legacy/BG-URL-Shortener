import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

// Data transfer object for URL shortening requests
export interface UrlDto {
  url: string;
  customAlias?: string;  // Optional field for custom short URLs
}

// Response object containing details about the shortened URL
export interface UrlResponseDto {
  originalUrl: string;   // The original long URL
  shortUrl: string;      // The generated short URL
  createdAt: string;     // Timestamp of creation
  clickCount: number;    // Number of times the URL was accessed
  customAlias?: string;  // Custom alias if one was specified
}

// Statistics interface for URL clicks
export interface UrlStats {
  clickCount: number;    // Number of times the URL was accessed
}

@Injectable({
  providedIn: 'root'  // Service is available application-wide
})
export class UrlService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Creates a shortened URL from a long URL
  shortenUrl(urlDto: UrlDto): Observable<UrlResponseDto> {
    return this.http.post<UrlResponseDto>(`${this.apiUrl}/shorten`, urlDto);
  }

  // Retrieves statistics for a specific shortened URL
  getUrlStats(shortId: string): Observable<UrlResponseDto> {
    return this.http.get<UrlResponseDto>(`${this.apiUrl}/stats/${shortId}`);
  }

  // Checks if a custom alias is available for use
  checkAliasAvailability(alias: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/alias-available/${alias}`);
  }

  // Creates a shortened URL with a specific custom alias
  createCustomUrl(originalUrl: string, customAlias: string): Observable<UrlResponseDto> {
    const urlDto: UrlDto = {
      url: originalUrl,
      customAlias: customAlias
    };
    return this.http.post<UrlResponseDto>(`${this.apiUrl}/shorten/custom`, urlDto);
  }

  // Validates a custom alias against backend rules
  validateCustomAlias(alias: string): Observable<{valid: boolean, message?: string}> {
    return this.http.post<{valid: boolean, message?: string}>(`${this.apiUrl}/validate-alias`, { alias });
  }
}