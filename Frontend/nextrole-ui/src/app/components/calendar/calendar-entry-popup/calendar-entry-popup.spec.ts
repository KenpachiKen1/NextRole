import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarEntryPopup } from './calendar-entry-popup';

describe('CalendarEntryPopup', () => {
  let component: CalendarEntryPopup;
  let fixture: ComponentFixture<CalendarEntryPopup>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalendarEntryPopup],
    }).compileComponents();

    fixture = TestBed.createComponent(CalendarEntryPopup);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
