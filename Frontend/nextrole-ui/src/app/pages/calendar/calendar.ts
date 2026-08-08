import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CalendarService } from '../../services/calendarService';
import { JobEntryService } from '../../services/jobEntry';
import { CalendarDay } from '../../models/calendar.models';
import {
  JobEntryResponse,
  CreateJobEntryRequest,
  UpdateJobEntryRequest,
} from '../../models/job-entry.model';
import { JobStatus } from '../../enums/jobEntry-status.enums';
import { Button } from '../../components/global/button/button';
import { DatePipe, NgClass } from '@angular/common';
import { JobStatusInfo } from '../../utilities/job-status-lookup';
import { CalendarEntryPopup } from '../../components/calendar/calendar-entry-popup/calendar-entry-popup';
import {
  CalendarAddEntryFlow,
  NewJobEntryPayload,
} from '../../components/calendar/calendar-add-entry-flow/calendar-add-entry-flow';
import {
  CalendarEditEntryForm,
  UpdateJobEntryPayload,
} from '../../components/calendar/calendar-edit-entry-flow/calendar-edit-entry-flow';
import { Router } from '@angular/router';
@Component({
  selector: 'app-calendar',
  imports: [
    Button,
    DatePipe,
    NgClass,
    CalendarEntryPopup,
    CalendarAddEntryFlow,
    CalendarEditEntryForm,
  ],
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

  private router = inject(Router);
  handleEnrichEntry(entry: JobEntryResponse) {
    this.closeDayPopup();
    this.router.navigate(['/ai-hub'], {
      queryParams: { entryId: entry.id, jobPostingId: entry.jobPostingId },
    });
  }
  year = signal(this.today.getFullYear());
  month = signal(this.today.getMonth());

  displayDate = computed(() => new Date(this.year(), this.month(), 1));

  grid = signal<CalendarDay[]>([]);

  selectedDay = signal<CalendarDay | null>(null);

  // non-null while the add-entry wizard is open; the date it was opened for
  newEntryDate = signal<Date | null>(null);

  // non-null while the edit form is open; the entry being edited
  editingEntry = signal<JobEntryResponse | null>(null);

  daysOfTheWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

  ngOnInit() {
    this.loadMonth();
  }

  // local-date key so entries land on the cell the user actually sees
  private dateKey(d: Date | string): string {
    const date = typeof d === 'string' ? new Date(d) : d;
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

  statusColor(status: JobStatus): string {
    return JobStatusInfo[status].color;
  }

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

    // render the grid immediately, then fill in entries when they arrive
    this.grid.set(days);

    this.jobEntryService.getEntries().subscribe({
      next: (entries) => {
        const byDate = new Map<string, JobEntryResponse[]>();

        for (const entry of entries) {
          const key = this.dateKey(entry.appliedAt);
          const bucket = byDate.get(key);
          bucket ? bucket.push(entry) : byDate.set(key, [entry]);
        }

        this.grid.set(
          days.map((day) => ({
            ...day,
            jobEntries: byDate.get(this.dateKey(day.date)) ?? [],
          })),
        );
      },
      error: (err) => console.error('getEntries() failed:', err),
    });
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
