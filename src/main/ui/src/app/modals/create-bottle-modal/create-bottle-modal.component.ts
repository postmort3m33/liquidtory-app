import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-create-bottle-modal',
  templateUrl: './create-bottle-modal.component.html',
  styleUrl: './create-bottle-modal.component.css'
})
export class CreateBottleModalComponent {

  // Constructor
  constructor(public dialogRef: MatDialogRef<CreateBottleModalComponent>) {}

  // The Form
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    capacityML: new FormControl('', Validators.required)
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