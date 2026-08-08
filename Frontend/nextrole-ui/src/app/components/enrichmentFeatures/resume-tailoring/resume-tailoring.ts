import { Component, inject, signal, computed, EventEmitter, Output, OnInit } from '@angular/core';
import { bedrockEnrichmentService } from '../../../services/bedrockEnrichmentService';
import { ResumeTailoringResponse } from '../../../models/bedrockAgents.model';
import { ResumeResponse } from '../../../models/resume.model';
import { ResumeService } from '../../../services/resumeService';
import { JobEntryResponse } from '../../../models/job-entry.model';
import { JobEntryService } from '../../../services/jobEntry';

@Component({
  selector: 'app-resume-tailoring',
  imports: [],
  templateUrl: './resume-tailoring.html',
  styleUrl: './resume-tailoring.css',
})
export class ResumeTailoring implements OnInit {
  private enrichmentService = inject(bedrockEnrichmentService);
  private resumeService = inject(ResumeService);
  private jobEntryService = inject(JobEntryService);

  @Output() close = new EventEmitter<void>();

  resumeId = signal<number | null>(null);
  jobPostingId = signal<number | null>(null);

  agentResponse = signal<ResumeTailoringResponse | null>(null);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  resumes: ResumeResponse[] = [];
  entries: JobEntryResponse[] = [];

  canSubmit = computed(
    () => this.resumeId() !== null && this.jobPostingId() !== null && !this.loading(),
  );

  ngOnInit() {
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
  }

  chooseJobPosting(id: number) {
    this.jobPostingId.set(id);
  }

  reset() {
    this.agentResponse.set(null);
    this.errorMessage.set(null);
  }

  cancel() {
    this.close.emit();
  }

  invokeTailoringAgent() {
    const resume = this.resumeId();
    const posting = this.jobPostingId();

    if (resume === null || posting === null) {
      this.errorMessage.set('Pick a resume and a job posting first.');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    this.enrichmentService.tailorResume(resume, posting).subscribe({
      next: (value) => {
        this.agentResponse.set(value);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Could not invoke agent and tailor resume', err);
        this.errorMessage.set('Something went wrong tailoring your resume. Try again.');
        this.loading.set(false);
      },
    });
  }
}
