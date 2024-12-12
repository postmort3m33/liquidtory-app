import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUserRootModalComponent } from './create-user-root-modal.component';

describe('CreateUserRootModalComponent', () => {
  let component: CreateUserRootModalComponent;
  let fixture: ComponentFixture<CreateUserRootModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateUserRootModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUserRootModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
