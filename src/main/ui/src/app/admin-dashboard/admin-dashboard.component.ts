import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import config from '../config';
import { jwtDecode } from 'jwt-decode';  // Import the jwt-decode library
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { CreateBottleModalComponent } from '../modals/create-bottle-modal/create-bottle-modal.component';
import * as XLSX from 'xlsx';
import { FormControl, FormGroup } from '@angular/forms';
import { AdminInventoryModalComponent } from '../modals/admin-inventory-modal/admin-inventory-modal.component';

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
  private getLastInventorySubmissionUrl: string = this.baseUrl + '/api/inventory/submit/last';
  private getAllInventorySubmissionsUrl: string = this.baseUrl + '/api/inventory/submit';
  private submitAdminInventoryActionUrl: string = this.baseUrl + '/api/inventory/admin';

  // Vars
  currentToken: string | null = null;
  activeTab: string = 'actions';
  userInfo!: UserInfo;
  liquorBottles: LiquorBottle[] = [];
  liquorBottleItems: LiquorBottleItem[] = [];
  isLoading: boolean = false;
  isOutside: boolean = false;
  inventorySearchForm!: FormGroup;
  currentFromVal!: string;
  currentToVal!: string;

  // Inventory Stuff
  lastInventorySubmissions!: LastInventorySubmissionResponse[];
  selectedInventorySubmission: LastInventorySubmissionResponse | null = null;

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

        // Subscribe to Date Range Form
        this.inventorySearchForm = new FormGroup({
          from: new FormControl(''),
          to: new FormControl('')
        });
        const inventorySearchFormChanges$ = this.inventorySearchForm.valueChanges;
        inventorySearchFormChanges$.subscribe(x => {
          this.currentFromVal = x.from;
          this.currentToVal = x.to;
        });

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
    this.httpClient.get<LastInventorySubmissionResponse[]>(this.getLastInventorySubmissionUrl, options)
      .subscribe(
        (response: LastInventorySubmissionResponse[]) => {

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
    dialogConfig.height = '50vh';
    dialogConfig.width = '80vw';
    dialogConfig.maxHeight = '50vh';
    dialogConfig.maxWidth = '80vw';

    // Open It.
    const dialogRef = this.dialog.open(CreateBottleModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();

  }

  // Open Admin Invenbtory Action Modal
  openAdminInventoryModal() {

    // Create new Dialog 
    const dialogConfig = new MatDialogConfig();

    // Vars
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '80vh';
    dialogConfig.width = '80vw';
    dialogConfig.maxHeight = '80vh';
    dialogConfig.maxWidth = '80vw';

    // Data
    dialogConfig.data = {
      liquorBottles: this.liquorBottles
    }

    // Open It.
    const dialogRef = this.dialog.open(AdminInventoryModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();
  }

  ///////////////
  // Inventory //
  ///////////////

  submitNewAdminInventoryAction() {

    // Open and subscribe to modal..
    this.openAdminInventoryModal().subscribe(result => {

      // If we gfot a result..
      if (result) {

        // Debug
        //console.log(result);

        // Create JSON Header..
        const options = {
          headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
        };

        // Post it
        this.httpClient.post(this.submitAdminInventoryActionUrl, result, options)
          .subscribe(
            (response: any) => {

            }
          )
      }
    });
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

  /////////////
  // Exports //
  /////////////

  exportSubmissionsExcel({ value, valid }: { value: InventorySearch, valid: boolean }) {

    //////////////////
    // Get the Data //
    //////////////////

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<InventorySubmission[]>(this.getAllInventorySubmissionsUrl + '?from=' + this.currentFromVal + '&to=' + this.currentToVal, options)
      .subscribe(
        (response: InventorySubmission[]) => {

          // Define the header row, including a single column for snapshots
          const header = ['ID', 'First Name', 'Last Name', 'Timestamp', 'Bar Name', 'Shots Used', 'Snapshots'];

          // Get a unique list of bar names
          const barNames = [...new Set(response.map(submission => submission.barName).filter(name => name))];

          // Create a new workbook
          const workbook: XLSX.WorkBook = XLSX.utils.book_new();

          // Function to adjust column widths based on content
          const autoFitColumns = (data: any[][]) => {
            return data[0].map((_, colIndex) => {
              const maxLength = data.reduce((max, row) => {
                const cell = row[colIndex] || '';
                const cellLength = cell.toString().length;
                return Math.max(max, cellLength);
              }, 0);
              return { width: maxLength + 2 }; // Add some padding
            });
          };

          if (barNames.length > 0) {
            // Create a worksheet for each barName
            barNames.forEach((barName) => {
              const barSubmissions = response.filter(submission => submission.barName === barName);

              // Map submissions to rows, formatting snapshots into a single string
              const barData = barSubmissions.map(submission => {
                // Group the snapshots by liquorBottleName and currentML, and count occurrences
                const snapshotCount = submission.snapshots?.reduce((acc, snapshot) => {
                  const key = `${snapshot.liquorBottleName}:${snapshot.currentML}ml`;
                  acc[key] = (acc[key] || 0) + 1; // Increment the count for this snapshot
                  return acc;
                }, {} as Record<string, number>) || {};

                // Create the snapshot string with counts
                const snapshotString = Object.entries(snapshotCount)
                  .map(([snapshot, count]) => (count > 1 ? `${snapshot} x${count}` : snapshot))
                  .join('; ') || 'No Snapshots';

                return [
                  submission.id,
                  submission.firstName,
                  submission.lastName,
                  submission.timestamp,
                  barName,
                  submission.numShotsUsed,
                  snapshotString,
                ];
              });

              // Combine the header and data
              const data = [header, ...barData];

              // Create the worksheet
              const worksheet: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(data);

              // Auto-fit columns for the worksheet
              worksheet['!cols'] = autoFitColumns(data);

              // Append the worksheet to the workbook, sanitizing the sheet name
              const sanitizeName = (name: string) => name.replace(/[:\/\\?\*\[\]]/g, '').substring(0, 31);
              XLSX.utils.book_append_sheet(workbook, worksheet, sanitizeName(barName));

            });
          } else {

            // If no bar names or submissions exist, create a blank sheet with just the header row
            const data = [header];
            const worksheet: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(data);
            // Auto-fit columns for the empty worksheet
            worksheet['!cols'] = autoFitColumns(data);
            XLSX.utils.book_append_sheet(workbook, worksheet, 'Empty Report');
          }

          // Generate the Excel file
          XLSX.writeFile(workbook, 'InventorySubmissions.xlsx');

        }
      );
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

export interface LastInventorySubmissionResponse {
  firstName: string;
  lastName: string;
  barId: number;
  timestamp: Date;
}

export interface InventorySubmission {
  id: number;
  firstName: string;
  lastName: string;
  barName: string;
  timestamp: Date;
  numShotsUsed: number;
  snapshots: InventorySnapshot[];
}

export interface InventorySnapshot {
  liquorBottleName: string;
  currentML: number;
}

export interface InventorySearch {
  from: string;
  to: string;
}