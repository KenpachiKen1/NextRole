import { Component, inject, signal, computed, EventEmitter, Output, OnInit } from '@angular/core';
import { bedrockEnrichmentService } from '../../../services/bedrockEnrichmentService';
import { ResumeFeedbackResponse } from '../../../models/bedrockAgents.model';
import { ResumeResponse } from '../../../models/resume.model';
import { ResumeService } from '../../../services/resumeService';
import { ResumePreviewer } from '../../resume/resume-previewer/resume-previewer';
import { NzProgressModule } from 'ng-zorro-antd/progress';

@Component({
  selector: 'app-resume-feedback',
  imports: [NzProgressModule, ResumePreviewer],
  templateUrl: './resume-feedback.html',
  styleUrl: './resume-feedback.css',
})
export class ResumeFeedback implements OnInit {
  private enrichmentService = inject(bedrockEnrichmentService);
  private resumeService = inject(ResumeService);

  @Output() close = new EventEmitter<void>();

  resumes: ResumeResponse[] = [];
  resumeId = signal<number | null>(null);
  agentResponse = signal<ResumeFeedbackResponse | null>(null);

  previewUrl = signal<string | null>(null);
  previewLoading = signal(false);

  loading = signal(false);
  errorMessage = signal<string | null>(null);

  // which resume the in-flight request was made for — lets us drop
  // a late response if the user switched resumes while it was running
  private pendingResumeId: number | null = null;

  progressValue = computed(() => this.agentResponse()?.overallResumeScore ?? 0);

  progressColor = computed(() => {
    const score = this.progressValue();
    if (score < 50) return { '0%': '#8C1D18', '100%': '#D9534F' };
    if (score < 75) return { '0%': '#D9534F', '100%': '#E8A33D' };
    return { '0%': '#7CB342', '100%': '#2E7D32' };
  });

  scoreLabel = computed(() => {
    const score = this.progressValue();
    if (score < 50) return 'Needs work';
    if (score < 75) return 'Decent';
    if (score < 90) return 'Strong';
    return 'Excellent';
  });

  canSubmit = computed(() => this.resumeId() !== null && !this.loading()); //no spam calls, need to enforce it on backend as well.

  ngOnInit(): void {
    this.resumeService.resumeList().subscribe({
      next: (response) => {
        this.resumes = Array.isArray(response) ? response : [response];
      },
      error: (err) => console.error('resumeList() failed:', err),
    });
  }

  chooseResumeId(id: number) {
    this.resumeId.set(id);
    // previous feedback belongs to a different resume
    this.agentResponse.set(null);
    this.errorMessage.set(null);
    this.loadPreview(id);
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

  reset() {
    this.agentResponse.set(null);
    this.errorMessage.set(null);
  }

  cancel() {
    this.reset();
    this.close.emit();
  }

  invokeResumeFeedbackAgent() {
    const resume = this.resumeId();

    if (resume === null) {
      this.errorMessage.set('A resume must be chosen in order to use this.');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);
    this.pendingResumeId = resume;

    this.enrichmentService.resumeFeedback(resume).subscribe({
      next: (response) => {
        if (this.pendingResumeId !== resume) return; // stale — user switched resumes
        this.agentResponse.set(response);
        this.loading.set(false);
      },
      error: (err) => {
        if (this.pendingResumeId !== resume) return;
        console.error('Could not invoke agent and review resume', err);
        this.errorMessage.set(
          'Error reviewing resume, please try again! Report the issue if it persists.',
        );
        this.loading.set(false);
      },
    });
  }
}
