import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Enrichment } from './enrichment';

describe('Enrichment', () => {
  let component: Enrichment;
  let fixture: ComponentFixture<Enrichment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Enrichment],
    }).compileComponents();

    fixture = TestBed.createComponent(Enrichment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
