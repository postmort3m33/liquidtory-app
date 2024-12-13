import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-create-bar-modal',
  templateUrl: './create-bar-modal.component.html',
  styleUrl: './create-bar-modal.component.css'
})
export class CreateBarModalComponent {

  // Constructor
  constructor(public dialogRef: MatDialogRef<CreateBarModalComponent>) {}

  // The Form
  form = new FormGroup({
    name: new FormControl('', Validators.required)
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
