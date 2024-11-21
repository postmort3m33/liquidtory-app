import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl } from "@angular/forms";

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
    name: new FormControl(''),
    capacityML: new FormControl('')
  });

  // On Close
  cancel() {
    this.dialogRef.close();
  }

  // On Save
  save() {
    this.dialogRef.close(this.form.value); // Passes form data back to the calling component
  }
}