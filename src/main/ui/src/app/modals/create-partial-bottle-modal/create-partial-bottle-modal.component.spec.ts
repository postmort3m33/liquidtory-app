import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePartialBottleModalComponent } from './create-partial-bottle-modal.component';

describe('CreatePartialBottleModalComponent', () => {
  let component: CreatePartialBottleModalComponent;
  let fixture: ComponentFixture<CreatePartialBottleModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreatePartialBottleModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatePartialBottleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
