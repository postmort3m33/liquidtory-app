import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import config from '../config';
import { jwtDecode } from 'jwt-decode';  // Import the jwt-decode library
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  // Use EC2 Instance IP when hosting..
  private baseURL: string = config.baseUrl;
  private authenticationUrl: string = this.baseURL + "/api/authenticate";

  // Vars
  username!: string;
  password!: string;
  token: string | null = null;
  errorMessage!: string;
  loginForm: FormGroup;

  // Constructor
  constructor(private httpClient: HttpClient, private router: Router, private fb: FormBuilder) {

    // Initialize the form group with validation rules
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {

    // Get Token..
    this.token = sessionStorage.getItem('jwtToken');
  }

  // Login Functions
  onLoginSubmit() {

    // check form Validation
    if (this.loginForm.valid) {

      // Clear error message..
      this.errorMessage = '';

      // Now popst it..
      this.httpClient.post<any>(this.authenticationUrl, this.loginForm.value)
        .subscribe({
          next: (response) => {

            // Save token to local storage..
            sessionStorage.setItem('jwtToken', response.jwt);

            // Decode the JWT token to get the role
            const decodedToken: any = jwtDecode(response.jwt); // Decode the JWT token

            // Set Role
            const userRole = decodedToken.role

            // Send to proper dashboards..
            if (userRole == "USER") {

              // Navigate to regular dashboard page..
              this.router.navigate(['/dashboard']);

            } else if (userRole == "ADMIN") {

              // Go to Admin Dashboard
              this.router.navigate(['/admin']);

            } else if (userRole == "ROOT") {

              // Go to Root Dashboard
              this.router.navigate(['/root']);
            }

          },
          error: (error) => {

            // Set error message
            this.errorMessage = "Bad Credentials!";

            // Reset form
            this.loginForm.reset();

            // Reset username and password
            this.username = '';
            this.password = '';

          }
        });

    } else {

      // Mark all touched
      this.loginForm.markAllAsTouched();

      // Set Error
      this.errorMessage = "Username and Password required!"
    }
  }
}