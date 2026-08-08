import { Component, inject, signal, computed, EventEmitter, Output, OnInit } from '@angular/core';
import { bedrockEnrichmentService } from '../../../services/bedrockEnrichmentService';
import { InterviewPrepResponse } from '../../../models/bedrockAgents.model';
import { ResumeResponse } from '../../../models/resume.model';
import { ResumeService } from '../../../services/resumeService';
import { JobEntryResponse } from '../../../models/job-entry.model';
import { JobEntryService } from '../../../services/jobEntry';

@Component({
  selector: 'app-interview-prep',
  imports: [],
  templateUrl: './interview-prep.html',
  styleUrl: './interview-prep.css',
})
export class InterviewPrep implements OnInit {
  private enrichmentService = inject(bedrockEnrichmentService);
  private resumeService = inject(ResumeService);
  private jobEntryService = inject(JobEntryService);

  @Output() close = new EventEmitter<void>();

  resumes: ResumeResponse[] = [];
  entries: JobEntryResponse[] = [];

  resumeId = signal<number | null>(null);
  jobPostingId = signal<number | null>(null);

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
  }

  chooseJobPosting(id: number) {
    this.jobPostingId.set(id);
    this.reset();
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
    this.errorMessage.set(null);
    this.pendingKey = key;

    this.enrichmentService.InvokeInterviewPrep(resume, posting).subscribe({
      next: (response) => {
        if (this.pendingKey !== key) return; 
        this.agentResponse.set(response);
        this.loading.set(false);
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
}
