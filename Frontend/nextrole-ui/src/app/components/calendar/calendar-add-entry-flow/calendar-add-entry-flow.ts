import { Component, EventEmitter, Input, OnInit, Output, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { JobStatus } from '../../../enums/jobEntry-status.enums';
import { JobStatusInfo } from '../../../utilities/job-status-lookup';
import { ResumeService } from '../../../services/resumeService';
import { ResumeResponse } from '../../../models/resume.model';
import { JobPostingService } from '../../../services/jobPosting';
import { JobPostingResponse } from '../../../models/job-posting.model';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common'; 

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
  | 'posting-details' 
  | 'add-posting-method'
  | 'manual-posting-form'
  | 'scrape-loading'
  | 'scrape-review'
  | 'pick-resume'
  | 'notes-and-status';

@Component({
  selector: 'app-calendar-add-entry-flow',
  imports: [DatePipe, FormsModule, CurrencyPipe],
  templateUrl: './calendar-add-entry-flow.html',
  styleUrl: './calendar-add-entry-flow.css',
})
export class CalendarAddEntryFlow implements OnInit {
  private resumeService = inject(ResumeService);
  private postingService = inject(JobPostingService);
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

  searchResults = signal<JobPostingResponse[]>([]);

  jobStatuses = Object.entries(JobStatusInfo).map(([key, info]) => ({
    key: key as JobStatus,
    ...info,
  }));

  searchTitle: string = '';
  ngOnInit() {
    // needed once the user reaches pick-resume, fetched up front so it's
    // ready by the time they get there, same pattern as the edit form
    this.resumeService.resumeList().subscribe({
      next: (response) => {
        this.resumes = Array.isArray(response) ? response : [response];
      },
      error: (err) => console.error('resumeList() failed:', err),
    });
  }

  selectedPosting = signal<JobPostingResponse | null>(null);

  viewPostingDetails(job: JobPostingResponse) {
    this.selectedPosting.set(job);
    this.goTo('posting-details');
  }

  confirmPostingSelection() {
    const job = this.selectedPosting();
    if (!job) return;
    this.jobPostingId.set(job.id);
    this.goTo('pick-resume');
  }

  goTo(step: FlowStep) {
    this.currentStep.set(step);
  }

  cancel() {
    this.close.emit();
  }

  searchJobPostings() {
    this.postingService.searchByTitle(this.searchTitle).subscribe({
      next: (response) => {
        console.log(response);
        Array.isArray(response)
          ? this.searchResults.set(response)
          : this.searchResults.set([response]); //should always be an array but checking to be sure
      },
      error: (err) => {
        console.error('Searching for jobs error: ', err);
      },
    });
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
