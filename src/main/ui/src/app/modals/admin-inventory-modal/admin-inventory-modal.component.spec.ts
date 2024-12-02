import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminInventoryModalComponent } from './admin-inventory-modal.component';

describe('AdminInventoryModalComponent', () => {
  let component: AdminInventoryModalComponent;
  let fixture: ComponentFixture<AdminInventoryModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminInventoryModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminInventoryModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
