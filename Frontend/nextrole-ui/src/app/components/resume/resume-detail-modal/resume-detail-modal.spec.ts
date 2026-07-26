import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeDetailModal } from './resume-detail-modal';

describe('ResumeDetailModal', () => {
  let component: ResumeDetailModal;
  let fixture: ComponentFixture<ResumeDetailModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResumeDetailModal],
    }).compileComponents();

    fixture = TestBed.createComponent(ResumeDetailModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
