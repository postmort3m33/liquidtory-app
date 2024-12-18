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
  currentHeight: number = 0;
  calculatedML: number = 0;
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
      ceil: 100,
      vertical: true,
      showSelectionBar: true,
      step: 5,
      translate: (value: number): string => {
        return value + '%';
      }
    };
  }

  // Numerical Integration for Volume of Revolution (up to the given height %)
  calculateVolumeAtHeight() {
    const dimensions = this.bottle.dimensions; // { height: %, radius: % }
    const heightCM = this.bottle.heightCM; // Total height in cm
    const diameterBottomCM = this.bottle.diameterBottomCM; // Bottom diameter in cm
    const radiusBottomCM = diameterBottomCM / 2; // Bottom radius in cm
    const step = 1; // Step size in percentage (smaller step = better accuracy)
    const sliceHeightCM = (step / 100) * heightCM;

    let volume = 0; // Initialize volume in mL (1 cm³ = 1 mL)

    // Iterate through height percentages (from 0% to the provided height percentage)
    for (let h = 1; h <= this.currentHeight; h += step) {
      // Find two points between which the current height percentage falls
      const prevDim = dimensions.find(dim => dim.height <= h);
      const nextDim = dimensions.find(dim => dim.height > h);

      // Interpolated radius percentage at height h
      let radiusPercent = 0;
      if (prevDim && nextDim) {
        // Linear interpolation for radius
        const heightDiff = nextDim.height - prevDim.height;
        const radiusDiff = nextDim.radius - prevDim.radius;
        const ratio = (h - prevDim.height) / heightDiff;
        radiusPercent = prevDim.radius + ratio * radiusDiff; // Interpolated radius in %
      } else if (prevDim) {
        // Use last known radius if no next dimension exists
        radiusPercent = prevDim.radius;
      }

      // Convert the radius percentage to an actual radius in cm
      const radiusCM = (radiusPercent / 100) * radiusBottomCM;
      //console.log("Step: ", h, " RadiusCM: ", radiusCM);

      // Add the volume of the slice using the formula for volume of revolution
      volume += Math.PI * Math.pow(radiusCM, 2) * sliceHeightCM; // Volume of a thin slice
      //console.log("New Volume: ", volume);
    }

    // The volume is directly in mL (since 1 cm³ = 1 mL)
    this.calculatedML = volume;
    //console.log('Calculated Volume (mL): ', this.calculatedML);
  }



  // On Close
  cancel() {
    this.dialogRef.close();
  }

  // On Save
  save() {
    this.dialogRef.close(this.calculatedML); // Passes form data back to the calling component
  }

}

export interface LiquorBottle {
  id: number;
  name: string;
  capacityML: number;
  heightCM: number;
  diameterBottomCM: number;
  dimensions: LiquorBottleDimension[];
}

export interface LiquorBottleDimension {
  height: number;
  radius: number;
}
