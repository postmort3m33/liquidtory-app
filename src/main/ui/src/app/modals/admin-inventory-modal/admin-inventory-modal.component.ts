import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-admin-inventory-modal',
  templateUrl: './admin-inventory-modal.component.html',
  styleUrl: './admin-inventory-modal.component.css'
})
export class AdminInventoryModalComponent implements OnInit {

  // Vars
  liquorBottles: LiquorBottle[] = [];
  errorMessage: string = "";

  // Constructor
  constructor(
    public dialogRef: MatDialogRef<AdminInventoryModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject the data passed to the modal
  ) {
    this.liquorBottles = data.liquorBottles;
  }

  // The Form
  form = new FormGroup({
    actionType: new FormControl('', Validators.required),
    liquorBottleId: new FormControl('', Validators.required),
    barName: new FormControl('', Validators.required),
    notes: new FormControl('')
  });

  ngOnInit(): void {
      
    // Sort Liquor Bottles..
    this.liquorBottles.sort((a, b) => a.name.localeCompare(b.name));
  }

  // On Close
  cancel() {
    this.dialogRef.close();
  }

  // On Save
  save() {

    // Make sure its valid..
    if (this.form.valid) {

      // Passes form data back to the calling component
      this.dialogRef.close(this.form.value);

    } else {

      // Show all Forms..
      this.form.markAllAsTouched();
    }
  }
}

export interface LiquorBottle {
  id: number;
  name: string;
  capacityML: number;
}
