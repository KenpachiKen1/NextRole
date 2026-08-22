import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { JobEntryResponse } from '../../../models/job-entry.model';
import { CalendarDay } from '../../../models/calendar.models';
import { DatePipe } from '@angular/common';
import { JobStatusInfo } from '../../../utilities/job-status-lookup';
import { FocusTrapDirective } from '../../../directives/focus-trap';
@Component({
  selector: 'app-calendar-entry-popup',
  imports: [DatePipe, FocusTrapDirective],
  templateUrl: './calendar-entry-popup.html',
  styleUrl: './calendar-entry-popup.css',
})
export class CalendarEntryPopup {
  @Input() selectedDay: CalendarDay | null = null;
  jobStatusInfo = JobStatusInfo;
  @Output() close = new EventEmitter<void>();

  @HostListener('document:keydown.escape')
  onEscape() {
    this.close.emit();
  }
  @Output() editEntry = new EventEmitter<JobEntryResponse>();
  @Output() deleteEntry = new EventEmitter<number>();
  @Output() addEntry = new EventEmitter<Date>();
  @Output() enrichEntry = new EventEmitter<JobEntryResponse>();
}
