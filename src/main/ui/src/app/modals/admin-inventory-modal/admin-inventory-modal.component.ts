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
  barNames: BarName[] = [];
  errorMessage: string = "";

  // Constructor
  constructor(
    public dialogRef: MatDialogRef<AdminInventoryModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject the data passed to the modal
  ) {
    this.liquorBottles = data.liquorBottles;
    this.barNames = data.barNames;
  }

  // The Form
  form = new FormGroup({
    actionType: new FormControl('', Validators.required),
    fullOrPartial: new FormControl('FULL', Validators.required),
    partialAmount: new FormControl('', [Validators.min(0), Validators.max(100)]),
    numFullBottles: new FormControl('', Validators.required),
    liquorBottleId: new FormControl('', Validators.required),
    barId: new FormControl('', Validators.required),
    notes: new FormControl('')
  });

  ngOnInit(): void {

    // Sort Liquor Bottles..
    this.liquorBottles.sort((a, b) => a.name.localeCompare(b.name));

    // Dynamically Vlidate Partial Amount..
    this.form.get('fullOrPartial')?.valueChanges.subscribe(value => {
      const partialAmountControl = this.form.get('partialAmount');
      const numBottlesControl = this.form.get('numFullBottles');
  
      if (value === 'PARTIAL') {

        // Set Partial Validation
        partialAmountControl?.setValidators([
          Validators.required,
          Validators.min(0),
          Validators.max(100)
        ]);

        // Clear Full Validation
        numBottlesControl?.clearValidators();

      } else if (value === 'FULL') {

        // Set Full Validation
        numBottlesControl?.setValidators(
          Validators.required
        );

        // Clear Partial Validators
        partialAmountControl?.clearValidators();
        
      }
  
      // Update Validators
      partialAmountControl?.updateValueAndValidity();
      numBottlesControl?.updateValueAndValidity();
    });
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

export interface BarName {
  id: number;
  name: string;
}
