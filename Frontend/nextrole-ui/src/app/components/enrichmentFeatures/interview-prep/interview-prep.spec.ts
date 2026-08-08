import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterviewPrep } from './interview-prep';

describe('InterviewPrep', () => {
  let component: InterviewPrep;
  let fixture: ComponentFixture<InterviewPrep>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InterviewPrep],
    }).compileComponents();

    fixture = TestBed.createComponent(InterviewPrep);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
