import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-change-password-modal',
  templateUrl: './change-password-modal.component.html',
  styleUrl: './change-password-modal.component.css'
})
export class ChangePasswordModalComponent {

  userId: number | null = null;
  passwordMismatch = false; // Flag for showing the error message

  constructor(
    public dialogRef: MatDialogRef<ChangePasswordModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.userId = data.userId;
  }

  // Form
  form = new FormGroup({
    userId: new FormControl<number | null>(null),
    password1: new FormControl('', [Validators.required]),
    password2: new FormControl('', [Validators.required])
  });

  // On Close
  cancel() {
    this.dialogRef.close();
  }

  // On Save
  save() {
    
    // Check if passwords match
    if (this.form.value.password1 !== this.form.value.password2) {
      this.passwordMismatch = true; // Show error message
      return;
    }

    // If valid, close dialog and return data
    if (this.form.valid) {

      // Save userID
      this.form.get('userId')?.setValue(this.userId);

      // now send it
      this.dialogRef.close(this.form.value);

    } else {
      this.form.markAllAsTouched(); // Show validation errors if fields are empty
    }
  }
}
