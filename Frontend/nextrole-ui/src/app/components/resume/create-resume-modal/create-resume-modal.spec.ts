import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateResumeModal } from './create-resume-modal';

describe('CreateResumeModal', () => {
  let component: CreateResumeModal;
  let fixture: ComponentFixture<CreateResumeModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateResumeModal],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateResumeModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
