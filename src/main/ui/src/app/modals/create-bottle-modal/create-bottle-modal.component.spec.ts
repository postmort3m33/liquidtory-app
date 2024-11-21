import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBottleModalComponent } from './create-bottle-modal.component';

describe('CreateBottleModalComponent', () => {
  let component: CreateBottleModalComponent;
  let fixture: ComponentFixture<CreateBottleModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateBottleModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateBottleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
