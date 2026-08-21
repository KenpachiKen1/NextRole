import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  inject,
  signal,
} from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';
import { JobEntryResponse, UpdateJobEntryRequest } from '../../../models/job-entry.model';
import { JobStatus } from '../../../enums/jobEntry-status.enums';
import { JobStatusInfo } from '../../../utilities/job-status-lookup';
import { ResumeService } from '../../../services/resumeService';
import { ResumeResponse } from '../../../models/resume.model';
import { JobPostingService } from '../../../services/jobPosting';
import { JobPostingResponse } from '../../../models/job-posting.model';

export interface UpdateJobEntryPayload {
  entryId: number;
  request: UpdateJobEntryRequest;
}

@Component({
  selector: 'app-edit-job-entry-form',
  standalone: true,
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: 'calendar-edit-entry-flow.html',
  styleUrl: 'calendar-edit-entry-flow.css',
})
export class CalendarEditEntryForm implements OnInit, OnChanges {
  private fb = inject(FormBuilder);
  private resumeService = inject(ResumeService);
  private jobPostingService = inject(JobPostingService);

  @Input() entry: JobEntryResponse | null = null;

  @Output() close = new EventEmitter<void>();
  @Output() submitted = new EventEmitter<UpdateJobEntryPayload>();

  resumes: ResumeResponse[] = [];

  postingDetails = signal<JobPostingResponse | null>(null);
  postingLoading = signal(false);
  postingError = signal('');

  jobStatuses = Object.entries(JobStatusInfo).map(([key, info]) => ({
    key: key as JobStatus,
    ...info,
  }));

  form = this.fb.nonNullable.group({
    resumeId: this.fb.nonNullable.control<number>(0, Validators.required),
    notes: [''],
    status: this.fb.nonNullable.control<JobStatus>(JobStatus.DRAFT),
  });

  ngOnInit() {
    this.resumeService.resumeList().subscribe({
      next: (response) => {
        this.resumes = Array.isArray(response) ? response : [response];
      },
      error: (err) => console.error('resumeList() failed:', err),
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['entry'] && this.entry) {
      this.form.patchValue({
        resumeId: this.entry.resumeId ?? 0,
        notes: this.entry.notes,
        status: this.entry.status,
      });

      this.loadPostingDetails(this.entry.jobPostingId);
    }
  }

  loadPostingDetails(jobPostingId: number) {
    this.postingDetails.set(null);
    this.postingError.set('');
    this.postingLoading.set(true);

    this.jobPostingService.getJobPostingById(jobPostingId).subscribe({
      next: (posting) => {
        this.postingDetails.set(posting);
        this.postingLoading.set(false);
      },
      error: (err) => {
        console.error('getJobPostingById() failed:', err);
        this.postingError.set('Could not load job posting details.');
        this.postingLoading.set(false);
      },
    });
  }

  submit() {
    if (!this.entry || this.form.invalid) {
      return;
    }

    const request: UpdateJobEntryRequest = this.form.getRawValue();

    this.submitted.emit({
      entryId: this.entry.id,
      request,
    });
  }
}
