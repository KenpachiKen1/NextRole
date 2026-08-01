import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CalendarService } from '../../services/calendarService';
import { JobEntryService } from '../../services/jobEntry';
import { CalendarDay } from '../../models/calendar.models';
import {
  JobEntryResponse,
  CreateJobEntryRequest,
  UpdateJobEntryRequest,
} from '../../models/job-entry.model';
import { Button } from '../../components/global/button/button';
import { DatePipe, NgClass } from '@angular/common';
import { JobStatusInfo } from '../../utilities/job-status-lookup';
import { CalendarEntryPopup } from '../../components/calendar/calendar-entry-popup/calendar-entry-popup';
import {
  CalendarAddEntryFlow,
  NewJobEntryPayload,
} from '../../components/calendar/calendar-add-entry-flow/calendar-add-entry-flow'
import {
  CalendarEditEntryForm,
  UpdateJobEntryPayload,
} from '../../components/calendar/calendar-edit-entry-flow/calendar-edit-entry-flow'

@Component({
  selector: 'app-calendar',
  imports: [Button, DatePipe, NgClass, CalendarEntryPopup, CalendarAddEntryFlow, CalendarEditEntryForm],
  templateUrl: './calendar.html',
  styleUrl: './calendar.css',
})
export class Calendar implements OnInit {
  private calendarService = inject(CalendarService);
  private jobEntryService = inject(JobEntryService);

  private today = new Date();

  jobStatuses = Object.entries(JobStatusInfo).map(([key, info]) => ({
    key,
    ...info,
  }));

  year = signal(this.today.getFullYear());
  month = signal(this.today.getMonth());

  displayDate = computed(() => new Date(this.year(), this.month(), 1));

  grid = signal<CalendarDay[]>([]);

  selectedDay = signal<CalendarDay | null>(null);

  // non-null while the add-entry wizard is open; the date it was opened for
  newEntryDate = signal<Date | null>(null);

  // non-null while the edit form is open; the entry being edited
  editingEntry = signal<JobEntryResponse | null>(null);

  ngOnInit() {
    this.loadMonth();
  }

  daysOfTheWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

  nextMonth() {
    if (this.month() === 11) {
      this.month.set(0);
      this.year.set(this.year() + 1);
    } else {
      this.month.set(this.month() + 1);
    }

    this.loadMonth();
  }

  prevMonth() {
    if (this.month() === 0) {
      this.month.set(11);
      this.year.set(this.year() - 1);
    } else {
      this.month.set(this.month() - 1);
    }

    this.loadMonth();
  }

  loadMonth() {
    const days = this.calendarService.getGridCalendarDays(this.year(), this.month());
    this.grid.set(days);
  }

  openDay(day: CalendarDay) {
    this.selectedDay.set(day);
  }

  closeDayPopup() {
    this.selectedDay.set(null);
  }

  openAddEntryFlow(date: Date) {
    this.newEntryDate.set(date);
  }

  closeAddEntryFlow() {
    this.newEntryDate.set(null);
  }

  addEntry(payload: NewJobEntryPayload) {
    const request: CreateJobEntryRequest = {
      jobPostingId: payload.jobPostingId,
      resumeId: payload.resumeId,
      notes: payload.notes,
      status: payload.status,
    };

    this.jobEntryService.createEntry(payload.jobPostingId, payload.resumeId, request).subscribe({
      next: () => {
        this.loadMonth();
        this.closeAddEntryFlow();
        this.closeDayPopup();
      },
      error: (err) => {
        console.error('createEntry() failed:', err);
      },
    });
  }

  updateEntry(entryId: number, request: UpdateJobEntryRequest) {
    this.jobEntryService.updateEntry(entryId, request).subscribe({
      next: () => {
        this.loadMonth();
        this.closeDayPopup();
        this.closeEditForm();
      },
      error: (err) => {
        console.error('updateEntry() failed:', err);
      },
    });
  }

  handleDeleteEntry(entryId: number) {
    this.jobEntryService.deleteEntry(entryId).subscribe({
      next: () => {
        this.loadMonth();
        this.closeDayPopup();
      },
      error: (err) => {
        console.error('deleteEntry() failed:', err);
      },
    });
  }

  handleEditEntry(entry: JobEntryResponse) {
    this.editingEntry.set(entry);
  }

  closeEditForm() {
    this.editingEntry.set(null);
  }

  handleEditSubmit(payload: UpdateJobEntryPayload) {
    this.updateEntry(payload.entryId, payload.request);
  }
}
