import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators, FormArray, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-create-bottle-modal',
  templateUrl: './create-bottle-modal.component.html',
  styleUrls: ['./create-bottle-modal.component.css']
})
export class CreateBottleModalComponent {

  // Constructor
  constructor(public dialogRef: MatDialogRef<CreateBottleModalComponent>) {}

  // The Form
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    capacityML: new FormControl('', Validators.required),
    heightCM: new FormControl('', Validators.required),
    diameterBottomCM: new FormControl('', Validators.required),
    dimensions: new FormArray(
      [this.createDimension()],
      this.minTwoDimensions
    )
  });

  // Creates a single dimension group
  createDimension(): FormGroup {
    return new FormGroup({
      height: new FormControl('', [Validators.required, Validators.pattern(/^\d+(\.\d+)?$/)]),
      radius: new FormControl('', [Validators.required, Validators.pattern(/^\d+(\.\d+)?$/)])
    });
  }

  // Get the FormArray for dimensions
  get dimensions(): FormArray {
    return this.form.get('dimensions') as FormArray;
  }

  // Add a new dimension to the array
  addDimension() {
    this.dimensions.push(this.createDimension());
    this.form.get('dimensions')?.updateValueAndValidity(); // Revalidate
  }

  // Remove a dimension from the array
  removeDimension(index: number) {
    if (this.dimensions.length > 1) { // Ensure at least one item remains
      this.dimensions.removeAt(index);
      this.form.get('dimensions')?.updateValueAndValidity(); // Revalidate
    }
  }

  // Custom Validator for at least 2 dimensions
  minTwoDimensions(control: AbstractControl): { [key: string]: any } | null {
    const array = control as FormArray;
    return array.length >= 2 ? null : { minTwoDimensions: true };
  }

  // On Close
  cancel() {
    this.dialogRef.close();
  }

  // On Save
  save() {
    // If Valid
    if (this.form.valid) {
      // Pass form data back to the calling component
      this.dialogRef.close(this.form.value);
    } else {
      // Mark all as touched
      this.form.markAllAsTouched();
    }
  }
}
