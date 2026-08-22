import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TermsModal } from './terms-modal';

describe('TermsModal', () => {
  let component: TermsModal;
  let fixture: ComponentFixture<TermsModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TermsModal],
    }).compileComponents();

    fixture = TestBed.createComponent(TermsModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
