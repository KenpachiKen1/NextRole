import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeFeedback } from './resume-feedback';

describe('ResumeFeedback', () => {
  let component: ResumeFeedback;
  let fixture: ComponentFixture<ResumeFeedback>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResumeFeedback],
    }).compileComponents();

    fixture = TestBed.createComponent(ResumeFeedback);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
