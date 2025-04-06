import { Component, ViewChild, ElementRef, Inject, ViewEncapsulation, AfterViewInit } from '@angular/core';
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
  imageUrl!: string;

  // Image Urls
  private bottleImageUrls: LiquorBottleImageUrl[] = [
    { id: 2, url: '../../../../assets/images/grey-goose-original-750.png' },
    { id: 1, url: '../../../../assets/images/jack-daniels-old-no-7-750.png' },
    { id: 3, url: '../../../../assets/images/bacardi-superior-1000.png' },
    { id: 4, url: '../../../../assets/images/blue-chair-bay-banana-rum-cream-750.png'},
    { id: 5, url: '../../../../assets/images/blue-chair-bay-spiced-rum-1000.png'},
    { id: 6, url: '../../../../assets/images/captain-morgan-original-spiced-rum-1000.png'},
    { id: 7, url: '../../../../assets/images/cruzan-aged-dark-rum-1000.png'},
    { id: 8, url: '../../../../assets/images/malibu-original-1000.png'},
    { id: 9, url: '../../../../assets/images/el-jimador-blanco-750.png'},
    { id: 10, url: '../../../../assets/images/myers-original-dark-rum-1000.png'},
    { id: 11, url: '../../../../assets/images/sailor-jerry-spiced-rum-1000.png'},
    { id: 12, url: '../../../../assets/images/zaya-gran-reserva-16-750.png'},
    { id: 37, url: '../../../../assets/images/buffalo-trace-bourbon-1000.png'},
    { id: 38, url: '../../../../assets/images/bulleit-bourbon-1000.png'},
    { id: 39, url: '../../../../assets/images/canadian-club-1858-1000.png'},
    { id: 97, url: '../../../../assets/images/deep-eddy-pineapple-vodka-750.png'}
  ];

  @ViewChild('bottleCanvas') bottleCanvas!: ElementRef<HTMLCanvasElement>;

  // Constructor
  constructor(
    public dialogRef: MatDialogRef<CreatePartialBottleModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject the data passed to the modal
  ) {
    // Get Bottle Data
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

    // Set the Image Url
    const bottleImageUrl = this.bottleImageUrls.find(b => b.id === this.bottle.id);
    if(bottleImageUrl) {
      this.imageUrl = bottleImageUrl.url;
    } else {
      // Load Default bottle
      this.imageUrl = '../../../../assets/images/empty-bottle.png';
    }
  }

  ngAfterViewInit(): void {
    //this.drawBottleOutline();
  }

  // Draw the bottle outline
  drawBottleOutline() {
    const canvas = this.bottleCanvas.nativeElement;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const dimensions = this.bottle.dimensions; // { height: %, radius: % }
    const canvasHeight = canvas.height;
    const canvasWidth = canvas.width;
    const centerX = canvasWidth / 2; // Center of the canvas
    const maxHeight = this.bottle.heightCM; // Max height of the bottle
    const maxRadius = this.bottle.diameterBottomCM / 2; // Max radius of the bottle

    // Clear the canvas
    ctx.clearRect(0, 0, canvasWidth, canvasHeight);

    // Calculate scaling factors based on canvas dimensions
    const scaleRatio = 0.6;
    const scaleHeight = (canvasHeight / maxHeight); // Scaling factor for height
    const scaleWidth = (canvasWidth / (maxRadius * 2)) * scaleRatio; // Scaling factor for width (using diameter)

    // Begin drawing the outline
    ctx.beginPath();
    ctx.moveTo(centerX, canvasHeight);

    // Draw the left side (based on height and scaled radius)
    dimensions.forEach((dim, index) => {
      const heightPx = (dim.height / 100) * maxHeight * scaleHeight; // Scaled height in pixels
      const radiusPx = (dim.radius / 100) * maxRadius * scaleWidth; // Scaled radius in pixels

      const y = canvasHeight - heightPx;
      if (index === 0) {
        ctx.lineTo(centerX - radiusPx, y); // Start the left curve
      } else {
        ctx.lineTo(centerX - radiusPx, y); // Continue drawing the left side
      }
    });

    // Mirror the right side
    for (let i = dimensions.length - 1; i >= 0; i--) {
      const dim = dimensions[i];
      const heightPx = (dim.height / 100) * maxHeight * scaleHeight; // Scaled height in pixels
      const radiusPx = (dim.radius / 100) * maxRadius * scaleWidth; // Scaled radius in pixels

      const y = canvasHeight - heightPx;
      ctx.lineTo(centerX + radiusPx, y); // Draw the right curve
    }

    ctx.closePath();
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 2;
    ctx.stroke();

    // Optional: Fill the liquid level (if needed)
    //this.fillLiquid(ctx, canvasWidth, canvasHeight);
  }

  // On slider change, recalculate and update liquid level
  onHeightChange() {
    this.calculateVolumeAtHeight();
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

    // Round the volume to 2 decimal places
    this.calculatedML = Math.round(volume * 100) / 100;
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

export interface LiquorBottleImageUrl {
  id: number;
  url: string;
}

