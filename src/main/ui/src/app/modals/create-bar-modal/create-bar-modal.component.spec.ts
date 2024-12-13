import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBarModalComponent } from './create-bar-modal.component';

describe('CreateBarModalComponent', () => {
  let component: CreateBarModalComponent;
  let fixture: ComponentFixture<CreateBarModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateBarModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateBarModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
