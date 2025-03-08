import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoPermissionModalComponent } from './no-permission-modal.component';

describe('NoPermissionModalComponent', () => {
  let component: NoPermissionModalComponent;
  let fixture: ComponentFixture<NoPermissionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NoPermissionModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NoPermissionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
