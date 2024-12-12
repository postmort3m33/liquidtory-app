import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-create-user-root-modal',
  templateUrl: './create-user-root-modal.component.html',
  styleUrl: './create-user-root-modal.component.css'
})
export class CreateUserRootModalComponent {

  // Vars
  allCompanies!: Company[];

  // The Form
  form = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    companyId: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required)
  });

  // Constructor
  constructor(
    public dialogRef: MatDialogRef<CreateUserRootModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject the data passed to the modal
  ) {
    this.allCompanies = data.companies;
  }

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

export interface Company {
  id: number;
  name: string;
}
