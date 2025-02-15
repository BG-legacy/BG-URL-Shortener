import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';

// Main test suite for AppComponent
describe('AppComponent', () => {
  // Set up testing environment before each test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],  // Import the component to be tested
    }).compileComponents();
  });

  // Test case: verify component creation
  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);  // Create component instance
    const app = fixture.componentInstance;                  // Get component reference
    expect(app).toBeTruthy();                             // Check if component exists
  });

  // Test case: verify component title property
  it(`should have the 'url-shortener-frontend' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('url-shortener-frontend');  // Check if title matches expected value
  });

  // Test case: verify title rendering in the template
  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();                              // Trigger change detection
    const compiled = fixture.nativeElement as HTMLElement;// Get access to DOM element
    expect(compiled.querySelector('h1')?.textContent)     // Check if h1 contains expected text
      .toContain('Hello, url-shortener-frontend');
  });
});
