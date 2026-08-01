import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarAddEntryFlow } from './calendar-add-entry-flow';

describe('CalendarAddEntryFlow', () => {
  let component: CalendarAddEntryFlow;
  let fixture: ComponentFixture<CalendarAddEntryFlow>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalendarAddEntryFlow],
    }).compileComponents();

    fixture = TestBed.createComponent(CalendarAddEntryFlow);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
