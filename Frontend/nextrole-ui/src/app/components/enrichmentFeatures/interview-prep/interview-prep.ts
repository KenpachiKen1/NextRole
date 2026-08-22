import { Component, inject, signal, computed, EventEmitter, HostListener, Output, OnInit, OnDestroy } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { bedrockEnrichmentService } from '../../../services/bedrockEnrichmentService';
import { InterviewPrepResponse } from '../../../models/bedrockAgents.model';
import { ResumeResponse } from '../../../models/resume.model';
import { ResumeService } from '../../../services/resumeService';
import { JobEntryResponse } from '../../../models/job-entry.model';
import { JobEntryService } from '../../../services/jobEntry';
import { JobPostingResponse } from '../../../models/job-posting.model';
import { JobPostingService } from '../../../services/jobPosting';
import { ResumePreviewer } from '../../resume/resume-previewer/resume-previewer';
import { Skeleton } from '../../global/skeleton/skeleton';
import { FocusTrapDirective } from '../../../directives/focus-trap';


import { NzProgressModule } from 'ng-zorro-antd/progress';

@Component({
  selector: 'app-interview-prep',
  imports: [ResumePreviewer, CurrencyPipe, NzProgressModule, Skeleton, FocusTrapDirective],
  templateUrl: './interview-prep.html',
  styleUrl: './interview-prep.css',
})
export class InterviewPrep implements OnInit, OnDestroy {
  private enrichmentService = inject(bedrockEnrichmentService);
  private resumeService = inject(ResumeService);
  private jobEntryService = inject(JobEntryService);
  private jobPostingService = inject(JobPostingService);

  @Output() close = new EventEmitter<void>();

  @HostListener('document:keydown.escape')
  onEscape() {
    this.cancel();
  }

  resumes: ResumeResponse[] = [];
  entries: JobEntryResponse[] = [];

  resumeId = signal<number | null>(null);
  jobPostingId = signal<number | null>(null);

  previewUrl = signal<string | null>(null);
  previewLoading = signal(false);

  selectedPosting = signal<JobPostingResponse | null>(null);
  postingLoading = signal(false);

  agentResponse = signal<InterviewPrepResponse | null>(null);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  // guards against a slow response landing after the user changed their picks
  private pendingKey: string | null = null;

  canSubmit = computed(
    () => this.resumeId() !== null && this.jobPostingId() !== null && !this.loading(),
  );

  ngOnInit(): void {
    this.resumeService.resumeList().subscribe({
      next: (response) => {
        this.resumes = Array.isArray(response) ? response : [response];
      },
      error: (err) => console.error('resumeList() failed:', err),
    });

    this.jobEntryService.getEntries().subscribe({
      next: (response) => {
        this.entries = Array.isArray(response) ? response : [response];
      },
      error: (err) => console.error('Could not load in your entries: ', err),
    });
  }

  chooseResume(id: number) {
    this.resumeId.set(id);
    this.reset();
    this.loadPreview(id);
  }

  chooseJobPosting(id: number) {
    this.jobPostingId.set(id);
    this.reset();
    this.loadPosting(id);
  }

  private loadPreview(id: number) {
    this.previewLoading.set(true);
    this.previewUrl.set(null);

    this.resumeService.viewSingleResume(id).subscribe({
      next: (response) => {
        if (this.resumeId() !== id) return; // user switched before this landed
        this.previewUrl.set(response.url);
        this.previewLoading.set(false);
      },
      error: (err) => {
        if (this.resumeId() !== id) return;
        console.error('viewSingleResume() failed:', err);
        this.previewLoading.set(false);
      },
    });
  }

  private loadPosting(id: number) {
    this.postingLoading.set(true);
    this.selectedPosting.set(null);

    this.jobPostingService.getJobPostingById(id).subscribe({
      next: (response) => {
        if (this.jobPostingId() !== id) return; // user switched before this landed
        this.selectedPosting.set(response);
        this.postingLoading.set(false);
      },
      error: (err) => {
        if (this.jobPostingId() !== id) return;
        console.error('getJobPostingById() failed:', err);
        this.postingLoading.set(false);
      },
    });
  }

  reset() {
    this.agentResponse.set(null);
    this.errorMessage.set(null);
  }

  cancel() {
    this.reset();
    this.close.emit();
  }

  invokeInterviewPrepAgent() {
    const resume = this.resumeId();
    const posting = this.jobPostingId();
    

    if (resume === null || posting === null) {
      this.errorMessage.set('A resume and a job posting are required to use this.');
      return;
    }

    const key = `${resume}:${posting}`;

    this.loading.set(true);
    this.startProgress();
    this.errorMessage.set(null);
    this.pendingKey = key;

    this.enrichmentService.InvokeInterviewPrep(resume, posting).subscribe({
      next: (response) => {
        if (this.pendingKey !== key) return; // stale — selection changed mid-flight
        this.agentResponse.set(response);
        this.loading.set(false);
        this.finishProgress();
      },
      error: (err) => {
        if (this.pendingKey !== key) return;
        console.error('Could not invoke agent and generate interview prep', err);
        this.errorMessage.set(
          'Error generating interview questions, please try again! Report the issue if it persists.',
        );
        this.loading.set(false);
      },
    });
  }

  progressPercent = signal(0);
  private progressTimer: ReturnType<typeof setInterval> | null = null;

  progressColor = { '0%': '#5B21B6', '100%': '#2E7D32' };

  private startProgress() {
    this.stopProgress();
    this.progressPercent.set(0);

    // eases toward 90% — the last 10% lands when the response does
    this.progressTimer = setInterval(() => {
      const current = this.progressPercent();
      if (current >= 90) return;
      const step = current < 60 ? 2 : 1;
      this.progressPercent.set(Math.min(90, current + step));
    }, 470);
  }

  private finishProgress() {
    this.stopProgress();
    this.progressPercent.set(100);
    setTimeout(() => this.progressPercent.set(0), 600);
  }

  private stopProgress() {
    if (this.progressTimer) {
      clearInterval(this.progressTimer);
      this.progressTimer = null;
    }
  }

  ngOnDestroy(): void {
    this.stopProgress();
  }
}
