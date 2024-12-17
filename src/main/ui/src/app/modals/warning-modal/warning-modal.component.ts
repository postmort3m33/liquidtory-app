import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-warning-modal',
  templateUrl: './warning-modal.component.html',
  styleUrl: './warning-modal.component.css'
})

export class WarningModalComponent {

  // Vars
  message: string | null = null;
  error: boolean = false;

  // Constructor
  constructor(
    public dialogRef: MatDialogRef<WarningModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject the data passed to the modal
  ) {
    this.message = data.message;
    this.error = data.error;
  }

  // Ok
  close() {
    this.dialogRef.close();
  }

}
