import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-create-company-modal',
  templateUrl: './create-company-modal.component.html',
  styleUrl: './create-company-modal.component.css'
})

export class CreateCompanyModalComponent {

  // Constructor
  constructor(public dialogRef: MatDialogRef<CreateCompanyModalComponent>) {}

  // The Form
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    email: new FormControl('', Validators.email),
    ownerPhone: new FormControl('', Validators.required),
    businessPhone: new FormControl('', Validators.required)
  });

  // On Close
  cancel() {
    this.dialogRef.close();
  }

  // On Save
  save() {

    // If Valid..
    if (this.form.valid) {

      // Passes form data back to the calling component
      this.dialogRef.close(this.form.value);
      
    } else {

      // Mark all as touched..
      this.form.markAllAsTouched();
    }
  }
}
