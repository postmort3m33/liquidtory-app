import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-create-user-modal',
  templateUrl: './create-user-modal.component.html',
  styleUrl: './create-user-modal.component.css'
})
export class CreateUserModalComponent {

  // Constructor
  constructor(public dialogRef: MatDialogRef<CreateUserModalComponent>) {}

  // The Form
  form = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required)
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
