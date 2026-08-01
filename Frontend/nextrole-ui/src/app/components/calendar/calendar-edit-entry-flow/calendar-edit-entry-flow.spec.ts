import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarEditEntryFlow } from './calendar-edit-entry-flow';

describe('CalendarEditEntryFlow', () => {
  let component: CalendarEditEntryFlow;
  let fixture: ComponentFixture<CalendarEditEntryFlow>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalendarEditEntryFlow],
    }).compileComponents();

    fixture = TestBed.createComponent(CalendarEditEntryFlow);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
