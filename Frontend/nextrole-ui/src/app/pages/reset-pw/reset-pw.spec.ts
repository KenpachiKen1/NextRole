import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetPw } from './reset-pw';

describe('ResetPw', () => {
  let component: ResetPw;
  let fixture: ComponentFixture<ResetPw>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetPw],
    }).compileComponents();

    fixture = TestBed.createComponent(ResetPw);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
