import { Component, ViewChild, ElementRef, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-create-partial-bottle-modal',
  templateUrl: './create-partial-bottle-modal.component.html',
  styleUrl: './create-partial-bottle-modal.component.css'
})
export class CreatePartialBottleModalComponent {
  @ViewChild('sliderThumb') sliderThumb!: ElementRef;

  // Vars
  bottle: LiquorBottle;
  currentML: number = 0;

  // Constructor
  constructor(
    public dialogRef: MatDialogRef<CreatePartialBottleModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject the data passed to the modal
  ) {
    this.bottle = data.bottle;
  }

  ngAfterViewInit() {
    this.sliderThumb.nativeElement.focus();
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
