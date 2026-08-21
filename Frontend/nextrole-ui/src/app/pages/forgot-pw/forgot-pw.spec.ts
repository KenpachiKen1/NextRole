import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotPw } from './forgot-pw';

describe('ForgotPw', () => {
  let component: ForgotPw;
  let fixture: ComponentFixture<ForgotPw>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgotPw],
    }).compileComponents();

    fixture = TestBed.createComponent(ForgotPw);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
