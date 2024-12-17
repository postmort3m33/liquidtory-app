import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import config from '../config';
import { jwtDecode } from 'jwt-decode';  // Import the jwt-decode library
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { CreateCompanyModalComponent } from '../modals/create-company-modal/create-company-modal.component';
import { CreateUserRootModalComponent } from '../modals/create-user-root-modal/create-user-root-modal.component';
import { WarningModalComponent } from '../modals/warning-modal/warning-modal.component';
import { CreateBottleModalComponent } from '../modals/create-bottle-modal/create-bottle-modal.component';

@Component({
  selector: 'app-root-dashboard',
  templateUrl: './root-dashboard.component.html',
  styleUrl: './root-dashboard.component.css'
})
export class RootDashboardComponent {

  // Constructor
  constructor(private router: Router, private httpClient: HttpClient, private dialog: MatDialog) { }

  // URLs
  private baseUrl: string = config.baseUrl;
  private userUrl: string = this.baseUrl + '/api/user';
  private companyUrl: string = this.baseUrl + '/api/company';
  private liquorBottleUrl: string = this.baseUrl + '/api/liquor';

  // Vars
  currentToken: string | null = null;
  activeTab: string = 'actions';
  userInfo!: UserInfo;

  // Init
  ngOnInit() {

    // Check if token exists in local storage
    this.currentToken = sessionStorage.getItem('jwtToken');

    // Decode Role from Token..
    if (this.currentToken != null) {

      // Get Role
      const decodedToken: any = jwtDecode(this.currentToken); // Decode the JWT token
      const userRole = decodedToken.role
      console.log('Token Role: ', userRole);

      // Check Role..
      if (userRole != 'ROOT') {

        // Redirect to home page..
        this.router.navigate(['home']);

      } else {

        // Get USer INfo
        this.getUserInfo();

      }

    } else {

      // Redirect to home page..
      this.router.navigate(['home']);
    }
  }

  // Get User Info from Token
  getUserInfo() {

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<UserInfo>(this.userUrl, options)
      .subscribe(
        (response: UserInfo) => {

          // Save User Info
          this.userInfo = response;

          // Debug
          //console.log(this.userInfo);
        }
      );
  }

  // Set Active Tab
  setActiveTab(tab: string) {

    // Set the tab
    this.activeTab = tab;
  }

  // Sign out function
  signOut() {

    // Clear the JWT token from sessionStorage
    sessionStorage.removeItem('jwtToken');

    // Optionally clear other user-related data if necessary
    sessionStorage.clear(); // Use this if you want to clear all sessionStorage

    // Redirect the user to the 'home' route
    this.router.navigate(['/home']);
  }

  ////////////
  // Modals //
  ////////////

  // Utility function to calculate modal width
  calculateModalWidth(screenWidth: number): number {
    // Define breakpoints and width range
    const minScreenWidth = 320; // Minimum screen width (e.g., mobile)
    const maxScreenWidth = 1920; // Maximum screen width (e.g., desktop)
    const minWidth = 80; // 80vw for smallest screen
    const maxWidth = 20; // 50vw for largest screen

    // Ensure screenWidth is clamped between minScreenWidth and maxScreenWidth
    const clampedScreenWidth = Math.max(minScreenWidth, Math.min(screenWidth, maxScreenWidth));

    // Calculate modal width as a percentage of the viewport width
    return (
      minWidth +
      (maxWidth - minWidth) *
      ((clampedScreenWidth - minScreenWidth) / (maxScreenWidth - minScreenWidth))
    );
  }

  // Open Create Company Modal
  openCreateCompanyModal() {

    // Get Window Width
    const screenWidth = window.innerWidth;

    // Calculate modal width using the utility function
    const modalWidthPercentage = this.calculateModalWidth(screenWidth);

    // Create new Dialog
    const dialogConfig = new MatDialogConfig();

    // Vars
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '85vh';
    dialogConfig.width = `${modalWidthPercentage}vw`;
    dialogConfig.maxHeight = '85vh';
    dialogConfig.maxWidth = `${modalWidthPercentage}vw`;

    // Open It.
    const dialogRef = this.dialog.open(CreateCompanyModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();
  }

  // Open Create User Root Modal
  openCreateUserModal(allCompanies: Company[]) {

    // Get Window Width
    const screenWidth = window.innerWidth;

    // Calculate modal width using the utility function
    const modalWidthPercentage = this.calculateModalWidth(screenWidth);

    // Create new Dialog
    const dialogConfig = new MatDialogConfig();

    // Vars
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '85vh';
    dialogConfig.width = `${modalWidthPercentage}vw`;
    dialogConfig.maxHeight = '85vh';
    dialogConfig.maxWidth = `${modalWidthPercentage}vw`;

    // Data
    dialogConfig.data = {
      companies: allCompanies
    }

    // Open It.
    const dialogRef = this.dialog.open(CreateUserRootModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();
  }

  // Warning Modal
  openWarningModal(messageToSend: string, errorToSend: boolean) {

    // Get Window Width
    const screenWidth = window.innerWidth;

    // Calculate modal width using the utility function
    const modalWidthPercentage = this.calculateModalWidth(screenWidth);

    // Create new Dialog
    const dialogConfig = new MatDialogConfig();

    // Vars
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '35vh';
    dialogConfig.width = `${modalWidthPercentage}vw`;
    dialogConfig.maxHeight = '35vh';
    dialogConfig.maxWidth = `${modalWidthPercentage}vw`;

    // Data
    dialogConfig.data = {
      message: messageToSend,
      error: errorToSend
    }

    // Open It.
    const dialogRef = this.dialog.open(WarningModalComponent, dialogConfig);

  }

  // Open Create Bottle Modal
  openCreateBottleModal() {

    // Get Window Width
    const screenWidth = window.innerWidth;

    // Calculate modal width using the utility function
    const modalWidthPercentage = this.calculateModalWidth(screenWidth);

    // Create new Dialog
    const dialogConfig = new MatDialogConfig();

    // Vars
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '55vh';
    dialogConfig.width = `${modalWidthPercentage}vw`;
    dialogConfig.maxHeight = '55vh';
    dialogConfig.maxWidth = `${modalWidthPercentage}vw`;

    // Open It.
    const dialogRef = this.dialog.open(CreateBottleModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();

  }

  /////////////
  // Methods //
  /////////////

  // Create New Company
  createNewCompany() {

    // Open and subscribe to modal..
    this.openCreateCompanyModal().subscribe(result => {

      // If we gfot a result..
      if (result) {

        // Create JSON Header..
        const options = {
          headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
        };

        // Post it
        this.httpClient.post(this.companyUrl, result, options)
          .subscribe({
            next: (response) => {

              // Show a message
              this.openWarningModal('Company Created!', false);

            },
            error: (error) => {

              // If Username Conflict
              if (error.status === 409) {

                // Show a message
                this.openWarningModal('Company Name Taken!', true);

              }
            }
          });
      }
    });


  }

  // Create New Company
  createNewUser() {

    /////////////////////////////
    // Get All Companies first //
    /////////////////////////////

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<Company[]>(this.companyUrl, options)
      .subscribe(
        (response: Company[]) => {

          // Open and subscribe to modal..
          this.openCreateUserModal(response).subscribe(result => {

            // If we gfot a result..
            if (result) {

              // Debug
              //console.log(result);

              // Create JSON Header..
              const options = {
                headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
              };

              // Post it
              this.httpClient.post(this.userUrl, result, options)
                .subscribe({
                  next: (response) => {

                    // Show a message
                    this.openWarningModal('User Created!', false);

                  },
                  error: (error) => {

                    // If Username Conflict
                    if (error.status === 409) {

                      // Show a message
                      this.openWarningModal('Username Taken!', true);

                    }
                  }
                });
            }
          });
        }
      );
  }

  // Create new Bottle
  createNewLiquorBottle() {

    // Open and subscribe to modal..
    this.openCreateBottleModal().subscribe(result => {

      // If we gfot a result..
      if (result) {

        // Create JSON Header..
        const options = {
          headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
        };

        // Post it
        this.httpClient.post(this.liquorBottleUrl, result, options)
          .subscribe({
            next: (response) => {

              // Show a message
              this.openWarningModal('Bottle Created!', false);

            },
            error: (error) => {

              // If Username Conflict
              if (error.status === 409) {

                // Show a message
                this.openWarningModal('Bottle already exists!', true);

              }
            }
          });
      }
    });

  }
}

// Interfaces
export interface UserInfo {
  firstName: string;
  lastName: string;
  company: string;
}

export interface Company {
  id: number;
  name: string;
}