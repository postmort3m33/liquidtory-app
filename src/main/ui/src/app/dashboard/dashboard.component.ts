import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import config from '../config';
import { CreatePartialBottleModalComponent } from '../modals/create-partial-bottle-modal/create-partial-bottle-modal.component';
import { jwtDecode } from 'jwt-decode';  // Import the jwt-decode library

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private dialog: MatDialog) { }

  // Url Vars
  private baseUrl: string = config.baseUrl;
  private userUrl: string = this.baseUrl + '/api/user';
  private liquorBottleUrl: string = this.baseUrl + '/api/liquor';
  private submitInventoryUrl: string = this.baseUrl + '/api/inventory/submit';
  private barUrl: string = this.baseUrl + '/api/bar';

  // Vars
  currentToken: string | null = null;
  userInfo!: UserInfo;
  liquorBottles: LiquorBottle[] = [];

  // Inventory Stuff
  liquorBottleItemsToSubmit: LiquorBottleItem[] = [];
  inventorySubmitted: boolean = false;
  lastInventorySubmission!: InventorySubmissionResponse;
  bars: Bar[] = [];
  selectedBar: Bar | null = null;
  showError: boolean = false;

  // Table Stuff
  activeTab: string = 'inventory';
  sortColumn: string | null = null;
  sortDirection: 'asc' | 'desc' = 'asc';

  // Init
  ngOnInit() {

    // Check if token exists in local storage
    this.currentToken = sessionStorage.getItem('jwtToken');

    // Decode Role from Token..
    if (this.currentToken != null) {

      // Get Role
      const decodedToken: any = jwtDecode(this.currentToken); // Decode the JWT token
      const userRole = decodedToken.role
      //console.log('Token Role: ', userRole);

      // Check Role..
      if (userRole != 'USER') {

        // Redirect to home page..
        this.router.navigate(['home']);

      } else {

        // Retrieve liquorBottleItemsToSubmit from session storage
        const storedItems = sessionStorage.getItem('liquorBottleItemsToSubmit');
        if (storedItems) {
          this.liquorBottleItemsToSubmit = JSON.parse(storedItems);
        }

        // Get USer INfo
        this.getUserInfo();

        // Get Bottles
        this.getLiquorBottles();

        // Get Bar Names
        this.getBars();
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

        }
      );
  }

  // Get Bars
  getBars() {

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<Bar[]>(this.barUrl, options)
      .subscribe({
        next: (response) => {

          // Save User Info
          this.bars = response;

        },
        error: (error) => {

        }
      });
  }

  // Submit this Inventory w Timestamp
  submitInventory() {

    // Only if a bar is chosen for inventory
    if (!this.selectedBar) {

      // Show error.
      this.showError = true;

    } else {

      // Reset show error
      this.showError = false

      // Create JSON Header..
      const options = {
        headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
      };

      // Define Bar Id
      let barId: number = this.selectedBar?.id ?? 0;

      // Create Response
      const submission: InventorySubmissionRequest = {
        liquorBottleItemsToSubmit: this.liquorBottleItemsToSubmit,
        barId: barId
      }

      // Post it
      this.httpClient.post<InventorySubmissionResponse>(this.submitInventoryUrl, submission, options)
        .subscribe(
          (response: InventorySubmissionResponse) => {

            /////////////////////
            // FUTURE ADDITION //
            /////////////////////
            // Set this up to receive error messages back from back-end

            // Set Response to Inventory Submission
            this.lastInventorySubmission = response;

            // Was submitted
            this.inventorySubmitted = true;

            // Reset Items to submit session storage..
            sessionStorage.removeItem('liquorBottleItemsToSubmit');
            this.liquorBottleItemsToSubmit = [];

          }
        );
    }
  }

  // Sign out function
  signOut() {

    // Clear the JWT token from sessionStorage
    sessionStorage.removeItem('jwtToken');

    // Optionally clear other user-related data if necessary
    //sessionStorage.clear(); // Use this if you want to clear all sessionStorage

    // Redirect the user to the 'home' route
    this.router.navigate(['/home']);
  }

  // Persists Temp inventory to session storage
  saveLiquorBottleItemsToStorage() {

    sessionStorage.setItem('liquorBottleItemsToSubmit', JSON.stringify(this.liquorBottleItemsToSubmit));
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

  // Open Partial Bottle Creator
  openCreatePartialBottleModal(bottleParam: LiquorBottle) {

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
      bottle: bottleParam
    }

    // Open It.
    const dialogRef = this.dialog.open(CreatePartialBottleModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();
  }

  //////////////////
  // Bottle Stuff //
  //////////////////

  // Get Bottles
  getLiquorBottles() {

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<LiquorBottle[]>(this.liquorBottleUrl, options)
      .subscribe(
        (response: LiquorBottle[]) => {

          // Save User Info
          this.liquorBottles = response;

          // Debug
          //console.log(this.liquorBottles);

        }
      );
  }

  /////////////////
  // Table Stuff //
  /////////////////

  // Sort Deliveries By
  sortInventory(column: keyof LiquorBottle) {

    // Determine sort direction based on the column
    if (this.sortColumn === column) {
      // Toggle sort direction if sorting by the same column
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      // Set the new column to sort by
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }

    // Sort the liquorBottles array
    this.liquorBottles.sort((a, b) => {
      const valueA = a[column];
      const valueB = b[column];

      // Sort in ascending or descending order
      if (valueA < valueB) {
        return this.sortDirection === 'asc' ? -1 : 1;
      } else if (valueA > valueB) {
        return this.sortDirection === 'asc' ? 1 : -1;
      } else {
        return 0;
      }
    });
  }

  // Method to count how many bottles are available for a given LiquorBottle type
  getFullBottleCount(bottle: LiquorBottle): number {

    // Filter liquorBottleItems to get only those that match the liquorBottleId
    const matchingItems = this.liquorBottleItemsToSubmit.filter(item => item.liquorBottleId === bottle.id && item.currentML === bottle.capacityML);

    // Return the length
    return matchingItems.length;
  }

  // Get Partial Bottle Count
  getPartialBottleCount(bottle: LiquorBottle): number {

    // Filter liquorBottleItems to get only those that match the liquorBottleId
    const matchingItems = this.liquorBottleItemsToSubmit.filter(item => item.liquorBottleId === bottle.id && item.currentML !== bottle.capacityML);

    // Return the length
    return matchingItems.length;
  }

  // Increase Inventory
  increaseFullInventory(bottle: LiquorBottle) {

    // Make New Request
    const liquorBottleItem: LiquorBottleItem = {
      liquorBottleId: bottle.id,
      currentML: bottle.capacityML
    }

    // Add to this variable...
    this.liquorBottleItemsToSubmit.push(liquorBottleItem);

    // Save to Session Storage
    this.saveLiquorBottleItemsToStorage();
  }

  // Decrease Inventory
  decreaseFullInventory(bottle: LiquorBottle) {

    // Find the index of the first item that matches both properties
    const indexToRemove = this.liquorBottleItemsToSubmit.findIndex(
      item => item.liquorBottleId === bottle.id && item.currentML === bottle.capacityML
    );

    // Remove the first occurrence if it exists
    if (indexToRemove !== -1) {
      this.liquorBottleItemsToSubmit.splice(indexToRemove, 1);
    }

    // Save to SessionStorage
    this.saveLiquorBottleItemsToStorage();
  }

  // Increase Partial Inventory
  increasePartialInventory(bottle: LiquorBottle) {

    // Open and subscribe to modal..
    this.openCreatePartialBottleModal(bottle).subscribe(result => {

      // If we gfot a result..
      if (result) {

        // Create JSON Header..
        const options = {
          headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
        };

        // Create Bottle to Return
        const partialBottle: LiquorBottleItem = {
          liquorBottleId: bottle.id,
          currentML: result
        }

        // Add to items to submit..
        this.liquorBottleItemsToSubmit.push(partialBottle);

        // Save to session storage
        this.saveLiquorBottleItemsToStorage();
      }
    });

  }

  // Decrease Partial Inventory
  resetPartialInventory() {

    // Remove all Partials..
    this.liquorBottleItemsToSubmit = this.liquorBottleItemsToSubmit.filter(item => {
      const matchingBottle = this.liquorBottles.find(bottle => bottle.id === item.liquorBottleId);
      return matchingBottle ? item.currentML === matchingBottle.capacityML : false;
    });

    // Save to sessionStorage
    this.saveLiquorBottleItemsToStorage();
  }
}

// Interfaces
export interface UserInfo {
  firstName: String;
  lastName: String;
  company: String;
}

export interface LiquorBottle {
  id: number;
  name: string;
  capacityML: number;
  heightCM: number;
  diameterBottomCM: number;
  dimensions: LiquorBottleDimension[];
}

export interface LiquorBottleDimension {
  height: number;
  radius: number;
}

export interface LiquorBottleItem {
  liquorBottleId: number;
  currentML: number;
}

export interface Bar {
  id: number;
  name: string;
  lastSubmission: LastInventorySubmissionResponse;
  liquorBottleItems: LiquorBottleItem[];
}

export interface LastInventorySubmissionResponse {
  firstName: string;
  lastName: string;
  barId: number;
  timestamp: Date;
}

export interface InventorySubmissionResponse {
  firstName: string;
  lastName: string;
  barId: number;
  timestamp: string;
}

export interface InventorySubmissionRequest {
  liquorBottleItemsToSubmit: LiquorBottleItem[];
  barId: number;
}