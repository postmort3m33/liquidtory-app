import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import config from '../config';
import { jwtDecode } from 'jwt-decode';  // Import the jwt-decode library
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { CreateBottleModalComponent } from '../modals/create-bottle-modal/create-bottle-modal.component';
import * as XLSX from 'xlsx';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AdminInventoryModalComponent } from '../modals/admin-inventory-modal/admin-inventory-modal.component';
import { CreateUserModalComponent } from '../modals/create-user-modal/create-user-modal.component';
import { CreateBarModalComponent } from '../modals/create-bar-modal/create-bar-modal.component';

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
  private barUrl: string = this.baseUrl + '/api/bar';
  private getAllInventorySubmissionsUrl: string = this.baseUrl + '/api/inventory/submit';
  private adminInventoryActionUrl: string = this.baseUrl + '/api/inventory/admin';
  private companyUsersUrl: string = this.baseUrl + '/api/company/users';

  // Vars
  currentToken: string | null = null;
  activeTab: string = 'actions';
  userInfo!: UserInfo;
  companyUsers: CompanyUser[] = [];
  liquorBottles: LiquorBottle[] = [];
  bars: Bar[] = [];
  selectedBar: Bar | null = null;
  inventorySearchForm!: FormGroup;
  currentFromVal!: string;
  currentToVal!: string;
  message: string | null = null;
  messageVisible: boolean = false; // Controls the CSS class
  messageError: boolean = false;

  // Table Stuff
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
      if (userRole != 'ADMIN') {

        // Redirect to home page..
        this.router.navigate(['home']);

      } else {

        // Subscribe to Date Range Form
        this.inventorySearchForm = new FormGroup({
          from: new FormControl('', Validators.required),
          to: new FormControl('', Validators.required)
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

        // Get Bars
        this.getBars();

        // Get Company Users
        this.getCompanyUsers();

      }

    } else {

      // Redirect to home page..
      this.router.navigate(['home']);
    }
  }

  showMessage(newMessage: string, error: boolean) {
    this.message = newMessage; 
    this.messageVisible = true; // Add "visible" class
    this.messageError = error;
    setTimeout(() => {
      this.messageVisible = false; // Remove "visible" class
      setTimeout(() => this.message = null, 500); // Delay nulling message to allow animation
    }, 5000); // Visible for 5 seconds
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

  // Get Users
  getCompanyUsers() {

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get deliveries..
    this.httpClient.get<CompanyUser[]>(this.companyUsersUrl, options)
      .subscribe({
        next: (response) => {

          // Save User Info
          this.companyUsers = response;

        },
        error: (error) => {

        }
      });

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

  // Open Admin Invenbtory Action Modal
  openAdminInventoryModal() {

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

    // Only Send All Bar Names and id..
    const barNames = this.bars.map(bar => ({ id: bar.id, name: bar.name }));

    // Data
    dialogConfig.data = {
      liquorBottles: this.liquorBottles,
      barNames: barNames
    }

    // Open It.
    const dialogRef = this.dialog.open(AdminInventoryModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();
  }

  // Open Create User Modal
  openCreateUserModal() {

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
    const dialogRef = this.dialog.open(CreateUserModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();
  }

  // Open Create Bar Modal
  openCreateBarModal() {

    // Get Window Width
    const screenWidth = window.innerWidth;

    // Calculate modal width using the utility function
    const modalWidthPercentage = this.calculateModalWidth(screenWidth);

    // Create new Dialog
    const dialogConfig = new MatDialogConfig();

    // Vars
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '45vh';
    dialogConfig.width = `${modalWidthPercentage}vw`;
    dialogConfig.maxHeight = '45vh';
    dialogConfig.maxWidth = `${modalWidthPercentage}vw`;

    // Open It.
    const dialogRef = this.dialog.open(CreateBarModalComponent, dialogConfig);

    // return the answer..
    return dialogRef.afterClosed();
  }

  ///////////////////////
  // Actions Functions //
  ///////////////////////

  // New Admin Inventory Action
  createNewAdminInventoryAction() {

    // Open and subscribe to modal..
    this.openAdminInventoryModal().subscribe(result => {

      // If we gfot a result..
      if (result) {

        // Create JSON Header..
        const options = {
          headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
        };

        // Post it
        this.httpClient.post(this.adminInventoryActionUrl, result, options)
          .subscribe({
            next: (response) => {

              // Show a message
              this.showMessage('Action Submitted!', false);

            },
            error: (error) => {

            }
          });
      }
    });
  }

  // Create New User
  createNewUser() {

    // Open and subscribe to modal..
    this.openCreateUserModal().subscribe(result => {

      // If we gfot a result..
      if (result) {

        // Create JSON Header..
        const options = {
          headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
        };

        // Post it
        this.httpClient.post(this.userUrl, result, options)
          .subscribe({
            next: (response) => {

              // Show a message
              this.showMessage('User Created!', false);

            },
            error: (error) => {

              // If Username Conflict
              if (error.status === 409) {

                // Show a message
                this.showMessage('Username Taken!', true);

              }
            }
          });
      }
    });

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
          .subscribe(
            (response: any) => {

            }
          )
      }
    });

  }

  // Create new Bar
  createNewBar() {

    // Open and subscribe to modal..
    this.openCreateBarModal().subscribe(result => {

      // If we gfot a result..
      if (result) {

        // Create JSON Header..
        const options = {
          headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
        };

        // Post it
        this.httpClient.post(this.barUrl, result, options)
          .subscribe({
            next: (response) => {

              // Show a message
              this.showMessage('Bar Created!', false);

            },
            error: (error) => {

              // If Username Conflict
              if (error.status === 409) {

                // Show a message
                this.showMessage('Bar Name Taken!', true);

              }
            }
          });
      }
    });
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

      // Get Bars
      this.getBars();

      // Reset Sorting..
      this.sortColumn = null;
      this.sortDirection = 'asc';

    } else if (tab == 'users') {

      // Get Users again
      this.getCompanyUsers();

      // Reset Sorting..
      this.sortColumn = null;
      this.sortDirection = 'asc';

    }

    // Set the tab
    this.activeTab = tab;
  }

  // Sort Deliveries By
  sortLiquorInventory(column: keyof LiquorBottle) {

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

  // Sort Deliveries By
  sortCompanyUsers(column: keyof CompanyUser) {

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
    this.companyUsers.sort((a, b) => {
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

    // If any items exist..
    if (this.selectedBar?.liquorBottleItems) {

      // Filter liquorBottleItems to get only those that match the liquorBottleId
      const matchingItems = this.selectedBar.liquorBottleItems.filter(item => item.liquorBottleId === bottle.id && item.currentML === bottle.capacityML);

      // Return the length
      return matchingItems.length;

    } else {

      // Return nothing
      return 0;
    }
  }

  // Get Partial Bottle Count
  getPartialBottleCount(bottle: LiquorBottle): number {

    // If any items exist..
    if (this.selectedBar?.liquorBottleItems) {

      // Filter liquorBottleItems to get only those that match the liquorBottleId
      const matchingItems = this.selectedBar.liquorBottleItems.filter(item => item.liquorBottleId === bottle.id && item.currentML !== bottle.capacityML);

      // Return the length
      return matchingItems.length;

    } else {

      // Return nothing
      return 0;
    }
  }

  /////////////
  // Exports //
  /////////////

  // Main Submission Export function
  exportSubmissionsExcel({ value, valid }: { value: InventorySearch, valid: boolean }) {

    // Is the for valid?
    if (!valid) {
      this.inventorySearchForm.markAllAsTouched();
      return;
    }

    //////////
    // Vars //
    //////////
    let adminActionSubmissions: AdminActionSubmission[] | null = null;
    let inventorySubmissions: InventorySubmission[] | null = null;

    // Create JSON Header..
    const options = {
      headers: new HttpHeaders().set("Authorization", "Bearer " + this.currentToken)
    };

    // Get Admin Actions
    this.httpClient.get<AdminActionSubmission[]>(this.adminInventoryActionUrl + '?from=' + this.currentFromVal + '&to=' + this.currentToVal, options)
      .subscribe(
        (response: AdminActionSubmission[]) => {
          adminActionSubmissions = response;
          // Once both requests are completed, create the Excel
          this.generateExcel(adminActionSubmissions, inventorySubmissions);
        }
      );

    // Get Inventory Submissions
    this.httpClient.get<InventorySubmission[]>(this.getAllInventorySubmissionsUrl + '?from=' + this.currentFromVal + '&to=' + this.currentToVal, options)
      .subscribe(
        (response: InventorySubmission[]) => {
          inventorySubmissions = response;
          // Once both requests are completed, create the Excel
          this.generateExcel(adminActionSubmissions, inventorySubmissions);
        }
      );
  }

  // Sub Function to generate the Excel sheet
  generateExcel(adminActionSubmissions: AdminActionSubmission[] | null, inventorySubmissions: InventorySubmission[] | null) {
    if (!adminActionSubmissions || !inventorySubmissions) {
      return; // Exit early if data isn't available yet
    }

    //////////////////
    // Create Excel //
    //////////////////
    const header = ['ID', 'First Name', 'Last Name', 'Timestamp', 'Action Type', 'Shots Used', 'Notes', 'Bottle(s)'];

    // Combine both submission arrays
    const combinedSubmissions = [
      ...adminActionSubmissions.map(action => ({
        ...action,
        type: 'Admin Action',  // Mark these as Admin Actions
        numShotsUsed: '',      // Admin actions don't have numShotsUsed
        snapshots: '',         // Admin actions don't have snapshots
      })),
      ...inventorySubmissions.map(inventory => ({
        ...inventory,
        type: 'Inventory Submission',  // Mark these as Inventory Submissions
        actionType: '',               // Inventory submissions don't have actionType
        bottleDesc: '',           // Inventory submissions don't have liquorBottleId
        notes: '',                    // Inventory submissions don't have notes
      }))
    ];

    // Is it empty?
    if (combinedSubmissions.length < 1) {
      
      // Show error
      this.showMessage('No submissions to export!', true);

      // Leave
      return;
    }

    // Helper function to parse the custom timestamp format
    const parseTimestamp = (timestamp: string): number => {
      const [date, time, period] = timestamp.split(' ');
      const [month, day] = date.split('-').map(Number);
      const [hours, minutes] = time.split(':').map(Number);
      const adjustedHours = period === 'PM' && hours !== 12 ? hours + 12 : period === 'AM' && hours === 12 ? 0 : hours;
      return new Date(new Date().getFullYear(), month - 1, day, adjustedHours, minutes).getTime();
    };

    // Sort combined submissions by parsed timestamp
    combinedSubmissions.sort((a, b) => parseTimestamp(b.timestamp) - parseTimestamp(a.timestamp));

    // Group submissions by barName
    const submissionsByBar = combinedSubmissions.reduce((group, submission) => {
      group[submission.barName] = group[submission.barName] || [];
      group[submission.barName].push(submission);
      return group;
    }, {} as Record<string, typeof combinedSubmissions>);

    // Create a new workbook
    const workbook: XLSX.WorkBook = XLSX.utils.book_new();

    // Iterate over each barName and create a worksheet
    Object.entries(submissionsByBar).forEach(([barName, submissions]) => {
      // Map submissions to rows
      const data = submissions.map(submission => {
        const isAdminAction = submission.type === 'Admin Action';

        // Check if snapshots is an array (only for Inventory Submissions)
        const snapshotString = Array.isArray(submission.snapshots)
          ? submission.snapshots.reduce((acc, snapshot) => {
            const key = `${snapshot.liquorBottleName}:${snapshot.currentML}ml`;
            acc[key] = (acc[key] || 0) + 1; // Increment the count for this combination
            return acc;
          }, {} as Record<string, number>) // Group by liquorBottleName and currentML
          : 'No Snapshots';

        // Convert the grouped snapshots into a string
        const snapshotFormattedString = Object.entries(snapshotString)
          .map(([key, count]) => count > 1 ? `${key} x${count}` : key) // Add count if more than 1
          .join('; ') || 'No Snapshots'; // Join by semicolon and return 'No Snapshots' if empty

        // Map the data based on whether it's an Admin Action or Inventory Submission
        return [
          submission.id,
          submission.firstName,
          submission.lastName,
          submission.timestamp,
          isAdminAction ? submission.actionType : '', // Show actionType for Admin Actions
          isAdminAction ? '' : submission.numShotsUsed, // Show numShotsUsed for Inventory Submissions
          isAdminAction ? submission.notes : '', // Show notes for Admin Actions
          isAdminAction ? submission.bottleDesc : snapshotFormattedString // Show snapshot info for Inventory Submissions
        ];
      });

      // Create a worksheet with the bar-specific data
      const worksheet: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet([header, ...data]);

      // Auto-fit columns for the worksheet
      worksheet['!cols'] = this.autoFitColumns([header, ...data]);

      // Append the worksheet to the workbook with the barName as the sheet name
      XLSX.utils.book_append_sheet(workbook, worksheet, barName);
    });

    // Generate the Excel file
    XLSX.writeFile(workbook, 'InventorySubmissions.xlsx');
  }

  // Function to adjust column widths based on content
  autoFitColumns(data: any[][]) {
    return data[0].map((_, colIndex) => {
      const maxLength = data.reduce((max, row) => {
        const cell = row[colIndex] || '';
        const cellLength = cell.toString().length;
        return Math.max(max, cellLength);
      }, 0);
      return { width: maxLength + 2 }; // Add some padding
    });
  }
}

export interface CompanyUser {
  id: number;
  firstName: string;
  lastName: string;
  username: string;
  role: string;
}

export interface UserInfo {
  firstName: string;
  lastName: string;
  company: string;
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

export interface InventorySubmission {
  id: number;
  firstName: string;
  lastName: string;
  barName: string;
  timestamp: string;
  numShotsUsed: number;
  snapshots: InventorySnapshot[];
}

export interface AdminActionSubmission {
  id: number;
  firstName: string;
  lastName: string;
  barName: string;
  timestamp: string;
  actionType: string;
  bottleDesc: string;
  notes: string;
}

export interface InventorySnapshot {
  liquorBottleName: string;
  currentML: number;
}

export interface InventorySearch {
  from: string;
  to: string;
}
