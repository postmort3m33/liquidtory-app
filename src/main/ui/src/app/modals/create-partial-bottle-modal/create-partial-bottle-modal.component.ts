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
    { id: 4, url: '../../../../assets/images/blue-chair-bay-banana-rum-cream-750.png' },
    { id: 5, url: '../../../../assets/images/blue-chair-bay-spiced-rum-1000.png' },
    { id: 6, url: '../../../../assets/images/captain-morgan-original-spiced-rum-1000.png' },
    { id: 7, url: '../../../../assets/images/cruzan-aged-dark-rum-1000.png' },
    { id: 8, url: '../../../../assets/images/malibu-original-1000.png' },
    { id: 102, url: '../../../../assets/images/malibu-original-1000.png' },
    { id: 9, url: '../../../../assets/images/el-jimador-blanco-750.png' },
    { id: 10, url: '../../../../assets/images/myers-original-dark-rum-1000.png' },
    { id: 11, url: '../../../../assets/images/sailor-jerry-spiced-rum-1000.png' },
    { id: 12, url: '../../../../assets/images/zaya-gran-reserva-16-750.png' },
    { id: 37, url: '../../../../assets/images/buffalo-trace-bourbon-1000.png' },
    { id: 38, url: '../../../../assets/images/bulleit-bourbon-1000.png' },
    { id: 39, url: '../../../../assets/images/canadian-club-1858-1000.png' },
    { id: 97, url: '../../../../assets/images/deep-eddy-pineapple-vodka-750.png' },
    { id: 91, url: '../../../../assets/images/barefoot-cabernet-sauvignon-1500.png' },
    { id: 92, url: '../../../../assets/images/barefoot-chardonnay-1500.png' },
    { id: 93, url: '../../../../assets/images/barefoot-merlot-1500.png' },
    { id: 94, url: '../../../../assets/images/barefoot-pinot-grigio-1500.png' },
    { id: 95, url: '../../../../assets/images/barefoot-white-zinfandel-1500.png' },
    { id: 103, url: '../../../../assets/images/lalo-blanco-1000.png' }
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
    if (bottleImageUrl) {
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

    const dimensions = [...this.bottle.dimensions];
    dimensions.sort((a, b) => a.height - b.height); // Ensure sorted by height

    const canvasHeight = canvas.height;
    const canvasWidth = canvas.width;
    const centerX = canvasWidth / 2;

    const maxHeight = this.bottle.heightCM;
    const maxRadius = this.bottle.diameterBottomCM / 2;

    const scaleRatio = 0.6;
    const scaleHeight = canvasHeight / maxHeight;
    const scaleWidth = (canvasWidth / (maxRadius * 2)) * scaleRatio;

    ctx.clearRect(0, 0, canvasWidth, canvasHeight);
    ctx.beginPath();

    // --- LEFT SIDE ---
    for (let h = 1; h <= 100; h++) {
      let prevDim = dimensions[0];
      let nextDim = dimensions[dimensions.length - 1];

      for (let i = 0; i < dimensions.length - 1; i++) {
        if (h >= dimensions[i].height && h <= dimensions[i + 1].height) {
          prevDim = dimensions[i];
          nextDim = dimensions[i + 1];
          break;
        }
      }

      let radiusPercent = prevDim.radius;
      const heightDiff = nextDim.height - prevDim.height;
      if (heightDiff !== 0) {
        const ratio = (h - prevDim.height) / heightDiff;
        const radiusDiff = nextDim.radius - prevDim.radius;
        radiusPercent = prevDim.radius + ratio * radiusDiff;
      }

      const radiusCM = (radiusPercent / 100) * maxRadius;
      const heightPx = (h / 100) * maxHeight * scaleHeight;
      const radiusPx = radiusCM * scaleWidth;
      const y = canvasHeight - heightPx;

      if (h === 1) {
        ctx.moveTo(centerX - radiusPx, y);
      } else {
        ctx.lineTo(centerX - radiusPx, y);
      }
    }

    // --- RIGHT SIDE (mirror) ---
    for (let h = 100; h >= 1; h--) {
      let prevDim = dimensions[0];
      let nextDim = dimensions[dimensions.length - 1];

      for (let i = 0; i < dimensions.length - 1; i++) {
        if (h >= dimensions[i].height && h <= dimensions[i + 1].height) {
          prevDim = dimensions[i];
          nextDim = dimensions[i + 1];
          break;
        }
      }

      let radiusPercent = prevDim.radius;
      const heightDiff = nextDim.height - prevDim.height;
      if (heightDiff !== 0) {
        const ratio = (h - prevDim.height) / heightDiff;
        const radiusDiff = nextDim.radius - prevDim.radius;
        radiusPercent = prevDim.radius + ratio * radiusDiff;
      }

      const radiusCM = (radiusPercent / 100) * maxRadius;
      const heightPx = (h / 100) * maxHeight * scaleHeight;
      const radiusPx = radiusCM * scaleWidth;
      const y = canvasHeight - heightPx;

      ctx.lineTo(centerX + radiusPx, y);
    }

    ctx.closePath();
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 2;
    ctx.stroke();
  }

  // On slider change, recalculate and update liquid level
  onHeightChange() {
    this.calculateVolumeAtHeight();
  }

  calculateVolumeAtHeight() {
    const dimensions = [...this.bottle.dimensions]; // Copy array to avoid mutation
    dimensions.sort((a, b) => a.height - b.height); // Ensure sorted by height

    const heightCM = this.bottle.heightCM;
    const diameterBottomCM = this.bottle.diameterBottomCM;
    const radiusBottomCM = diameterBottomCM / 2;
    const step = 1; // Step in %

    const sliceHeightCM = (step / 100) * heightCM;
    let volume = 0;

    for (let h = 1; h <= this.currentHeight; h += step) {
      let prevDim = dimensions[0];
      let nextDim = dimensions[dimensions.length - 1];

      // Get correct Prev and Next Dimensions
      for (let i = 0; i < dimensions.length - 1; i++) {
        if (h >= dimensions[i].height && h <= dimensions[i + 1].height) {
          prevDim = dimensions[i];
          nextDim = dimensions[i + 1];
          break;
        }
      }

      // interpolate Radius
      let radiusPercent = prevDim.radius;
      const heightDiff = nextDim.height - prevDim.height;
      if (heightDiff !== 0) {
        const ratio = (h - prevDim.height) / heightDiff;
        const radiusDiff = nextDim.radius - prevDim.radius;
        radiusPercent = prevDim.radius + ratio * radiusDiff;
      }

      // Set Values
      const radiusCM = (radiusPercent / 100) * radiusBottomCM;
      volume += Math.PI * Math.pow(radiusCM, 2) * sliceHeightCM;
    }

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

