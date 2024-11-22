import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import config from '../config';
import { jwtDecode } from 'jwt-decode';  // Import the jwt-decode library
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { CreateBottleModalComponent } from '../modals/create-bottle-modal/create-bottle-modal.component';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {

  // Constructor
  constructor(private router: Router, private httpClient: HttpClient, private dialog: MatDialog) { }

  // URLS
  private baseUrl: string = config.baseUrl;
  private userUrl: string = this.baseUrl + '/api/user';
  private liquorBottleUrl: string = this.baseUrl + '/api/liquor';
  private liquorInventoryUrl: string = this.baseUrl + '/api/inventory/liquor';
  private removeLiquorBottleItemUrl: string = this.baseUrl + '/api/inventory/liquor/remove';
  private getLastInventorySubmissionUrl: string = this.baseUrl + '/api/inventory/submit/last'


  // Vars
  currentToken: string | null = null;
  activeTab: string = 'actions';
  userInfo!: UserInfo;
  liquorBottles: LiquorBottle[] = [];
  liquorBottleItems: LiquorBottleItem[] = [];
  isLoading: boolean = false;
  isOutside: boolean = false;

  // Inventory Stuff
  lastInventorySubmissions!: InventorySubmissionResponse[];
  selectedInventorySubmission: InventorySubmissionResponse | null = null;

  // Table Stuff
  sortColumn: string | null = null;
  sortDirection: 'asc' | 'desc' = 'asc';
  selectedBarId: number = 1;

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
      if (userRole != 'ADMIN') {

        // Redirect to home page..
        this.router.navigate(['home']);

      } else {

        // Get USer INfo
        this.getUserInfo();

        // Get Bottles
        this.getLiquorBottles();

        // Get Inventory
        this.getLiquorBottleItems();

        // Get Last Submission
        this.getLastInventorySubmissions();
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

  // Get Last Submission
  getLastInventorySubmissions() {

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<InventorySubmissionResponse[]>(this.getLastInventorySubmissionUrl, options)
      .subscribe(
        (response: InventorySubmissionResponse[]) => {

          // Save User Info
          this.lastInventorySubmissions = response;

          // Debug
          //console.log('Last Submissions: ', this.lastInventorySubmissions);
        }
      );

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

  // Get Last

  ////////////
  // Modals //
  ////////////

  // Open Create Bottle Modal
  openCreateBottleModal() {

    // Create new Dialog 
    const dialogConfig = new MatDialogConfig();

    // Vars
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '60vh';
    dialogConfig.width = '80vw';
    dialogConfig.maxHeight = '60vh';
    dialogConfig.maxWidth = '80vw';

    // Open It.
    const dialogRef = this.dialog.open(CreateBottleModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();

  }

  //////////////////
  // Bottle Stuff //
  //////////////////

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
          .subscribe(
            (response: any) => {

            }
          )
      }
    });

  }

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
          //console.log('Liquor Bottles: ', this.liquorBottles);
        }
      );
  }

  // Get Liquor Inventory
  getLiquorBottleItems() {

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<LiquorBottleItem[]>(this.liquorInventoryUrl, options)
      .subscribe(
        (response: LiquorBottleItem[]) => {

          // Save User Info
          this.liquorBottleItems = response;

          // Debug
          //console.log('Liquor Bottles Items: ', this.liquorBottleItems);
        }
      );
  }

  /////////////////
  // Table Stuff //
  /////////////////

  // Set Active Tab
  setActiveTab(tab: string) {

    // If Inventory Table..
    if (tab == 'inventory') {

      // Get Bottles again
      this.getLiquorBottles();

      // Get INventory Again
      this.getLiquorBottleItems();

      // Get Last Submissions again.
      this.getLastInventorySubmissions();

      // run on barChage
      this.onBarChange();

      // Reset Sorting..
      this.sortColumn = null;
      this.sortDirection = 'asc';
    }

    // Set the tab
    this.activeTab = tab;
  }

  // when changing Bar Inventories
  onBarChange() {

    // Change selected Bar..
    if (this.isOutside) {

      // Set Inventory Submission
      const foundSubmission = this.lastInventorySubmissions.find(submission => submission.barId === 2);

      if (foundSubmission) {

        //Set it
        this.selectedInventorySubmission = foundSubmission;

      } else {

        // Set Null
        this.selectedInventorySubmission = null;
      }

      // Set Selected Bar Id
      this.selectedBarId = 2;

    } else {

      // Set Inventory Submission
      const foundSubmission = this.lastInventorySubmissions.find(submission => submission.barId === 1);

      if (foundSubmission) {

        //Set it
        this.selectedInventorySubmission = foundSubmission;

      } else {

        // Set null
        this.selectedInventorySubmission = null;
      }

      // Set Selected
      this.selectedBarId = 1;

    }
  }

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
    const matchingItems = this.liquorBottleItems.filter(item => item.liquorBottleId === bottle.id && item.currentML === bottle.capacityML && item.barId === this.selectedBarId);

    // Return the length
    return matchingItems.length;
  }

  // Get Partial Bottle Count
  getPartialBottleCount(bottle: LiquorBottle): number {

    // Filter liquorBottleItems to get only those that match the liquorBottleId
    const matchingItems = this.liquorBottleItems.filter(item => item.liquorBottleId === bottle.id && item.currentML !== bottle.capacityML && item.barId === this.selectedBarId);

    // Return the length
    return matchingItems.length;
  }
}

export interface UserInfo {
  firstName: string;
  lastName: string;
}

export interface LiquorBottle {
  id: number;
  name: string;
  capacityML: number;
}

export interface LiquorBottleItem {
  liquorBottleId: number;
  currentML: number;
  barId: number;
}

export interface InventorySubmissionResponse {
  firstName: string;
  lastName: string;
  barId: number;
  timestamp: Date;
}