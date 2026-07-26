import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeCard } from './resume-card';

describe('ResumeCard', () => {
  let component: ResumeCard;
  let fixture: ComponentFixture<ResumeCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResumeCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ResumeCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
