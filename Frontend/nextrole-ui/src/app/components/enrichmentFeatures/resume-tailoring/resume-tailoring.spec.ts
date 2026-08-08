import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeTailoring } from './resume-tailoring';

describe('ResumeTailoring', () => {
  let component: ResumeTailoring;
  let fixture: ComponentFixture<ResumeTailoring>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResumeTailoring],
    }).compileComponents();

    fixture = TestBed.createComponent(ResumeTailoring);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
