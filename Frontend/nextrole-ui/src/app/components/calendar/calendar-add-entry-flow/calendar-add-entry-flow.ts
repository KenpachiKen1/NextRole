import { Component, EventEmitter, Input, OnInit, Output, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { JobStatus } from '../../../enums/jobEntry-status.enums';
import { JobStatusInfo } from '../../../utilities/job-status-lookup';
import { ResumeService } from '../../../services/resumeService';
import { ResumeResponse } from '../../../models/resume.model';

export interface NewJobEntryPayload {
  jobPostingId: number;
  resumeId: number;
  notes: string;
  status: JobStatus;
  date: Date;
}

type FlowStep =
  | 'choose-posting'
  | 'search-posting'
  | 'add-posting-method'
  | 'manual-posting-form'
  | 'scrape-loading'
  | 'scrape-review'
  | 'pick-resume'
  | 'notes-and-status';

@Component({
  selector: 'app-calendar-add-entry-flow',
  imports: [DatePipe],
  templateUrl: './calendar-add-entry-flow.html',
  styleUrl: './calendar-add-entry-flow.css',
})
export class CalendarAddEntryFlow implements OnInit {
  private resumeService = inject(ResumeService);

  @Input() initialDate: Date | null = null;

  @Output() close = new EventEmitter<void>();
  @Output() submitted = new EventEmitter<NewJobEntryPayload>();

  currentStep = signal<FlowStep>('choose-posting');

  jobPostingId = signal<number | null>(null);
  resumeId = signal<number | null>(null);

  // draft fields for the final step
  notes = signal('');
  status = signal<JobStatus>(JobStatus.DRAFT);

  resumes: ResumeResponse[] = [];

  jobStatuses = Object.entries(JobStatusInfo).map(([key, info]) => ({
    key: key as JobStatus,
    ...info,
  }));

  ngOnInit() {
    // needed once the user reaches pick-resume — fetched up front so it's
    // ready by the time they get there, same pattern as the edit form
    this.resumeService.resumeList().subscribe({
      next: (response) => {
        this.resumes = Array.isArray(response) ? response : [response];
      },
      error: (err) => console.error('resumeList() failed:', err),
    });
  }

  goTo(step: FlowStep) {
    this.currentStep.set(step);
  }

  cancel() {
    this.close.emit();
  }

  selectExistingPosting(id: number) {
    this.jobPostingId.set(id);
    this.goTo('pick-resume');
  }

  // TODO: real handoff once manual-posting-form / scrape-review actually
  // create a posting server-side and get back a real id
  confirmNewPosting(id: number) {
    this.jobPostingId.set(id);
    this.goTo('pick-resume');
  }

  selectResume(id: number) {
    this.resumeId.set(id);
    this.goTo('notes-and-status');
  }

  submit() {
    const postingId = this.jobPostingId();
    const resume = this.resumeId();

    if (postingId === null || resume === null || !this.initialDate) {
      console.error('Cannot submit — missing jobPostingId, resumeId, or date');
      return;
    }

    this.submitted.emit({
      jobPostingId: postingId,
      resumeId: resume,
      notes: this.notes(),
      status: this.status(),
      date: this.initialDate,
    });
  }
}
