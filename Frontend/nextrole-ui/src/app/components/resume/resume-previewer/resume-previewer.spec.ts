import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumePreviewer } from './resume-previewer';

describe('ResumePreviewer', () => {
  let component: ResumePreviewer;
  let fixture: ComponentFixture<ResumePreviewer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResumePreviewer],
    }).compileComponents();

    fixture = TestBed.createComponent(ResumePreviewer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
