import { Component, ViewChild, ElementRef, Inject, ViewEncapsulation } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Options } from '@angular-slider/ngx-slider';

@Component({
  selector: 'app-create-partial-bottle-modal',
  templateUrl: './create-partial-bottle-modal.component.html',
  styleUrl: './create-partial-bottle-modal.component.css',
  encapsulation: ViewEncapsulation.None
})
export class CreatePartialBottleModalComponent {

  // Vars
  bottle: LiquorBottle;
  currentML: number = 0;
  options!: Options;

  // Constructor
  constructor(
    public dialogRef: MatDialogRef<CreatePartialBottleModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject the data passed to the modal
  ) {
    this.bottle = data.bottle;

    // Initialize options after `bottle` is assigned
    this.options = {
      floor: 0,
      ceil: this.bottle.capacityML,
      vertical: true,
      showSelectionBar: true,
      translate: (value: number): string => {
        return value + ' mL';
      }
    };
  }

  // On Close
  cancel() {
    this.dialogRef.close();
  }

  // On Save
  save() {
    this.dialogRef.close(this.currentML); // Passes form data back to the calling component
  }

}

export interface LiquorBottle {
  id: number;
  name: string;
  capacityML: number;
}
