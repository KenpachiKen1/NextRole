import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface EnrichmentOption {
  key: 'tailoring' | 'interview-prep' | 'feedback';
  title: string;
  description: string;
  color: string;
  features: string[];
  limitations: string[];
}

@Component({
  selector: 'app-enrichment',
  imports: [],
  templateUrl: './enrichment.html',
  styleUrl: './enrichment.css',
})
export class Enrichment {
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  // set when arriving from a calendar entry; null on direct navigation
  entryId = signal<number | null>(null);
  jobPostingId = signal<number | null>(null);

  options: EnrichmentOption[] = [
    {
      key: 'tailoring',
      title: 'Resume Tailoring',
      description:
        'Rewrite your resume against this specific posting — reframing what you already have to match how the hiring team reads it.',
      color: '#5B21B6',
      features: [
        'Reframes existing bullets toward the posting',
        "Suggests stronger phrasing for what you've written",
        'Highlights experience worth moving higher',
        "Aligns your wording with the posting's language",
      ],
      limitations: [
        "Will not suggest adding keywords you don't already have",
        "Will not invent or recommend experience you haven't listed",
        'Works purely from your resume as written',
      ],
    },
    {
      key: 'interview-prep',
      title: 'Interview Prep',
      description:
        "Generate likely questions for this role and rehearse your answers before you're in the room.",
      color: '#0F766E',
      features: [
        'Role-specific technical and behavioral questions',
        'Talking points drawn from your own resume',
        'Company and team context from the posting',
        'Questions worth asking your interviewer',
      ],
      limitations: [
        "Cannot predict the actual questions you'll be asked",
        "No insight into this company's specific interview process",
        'Answers are starting points, not scripts',
      ],
    },
    {
      key: 'feedback',
      title: 'Resume Feedback',
      description:
        'A general critique of your resume — structure, clarity, and impact — independent of any single job posting.',
      color: '#9F1239',
      features: [
        'Bullet-level rewrites for clarity and impact',
        'Flags vague or low-signal phrasing',
        'Checks formatting and section ordering',
        'Identifies gaps a recruiter would notice',
      ],
      limitations: [
        'Not tailored to any particular job posting',
        'Cannot verify claims or confirm accuracy',
        'No guarantee of passing any specific ATS',
      ],
    },
  ];
  constructor() {
    const params = this.route.snapshot.queryParamMap;
    const entry = params.get('entryId');
    const posting = params.get('jobPostingId');

    if (entry) this.entryId.set(Number(entry));
    if (posting) this.jobPostingId.set(Number(posting));
  }

  start(option: EnrichmentOption) {
    // TODO: wire to the backend enrichment endpoints
    console.log('start', option.key, this.entryId(), this.jobPostingId());
  }
}
