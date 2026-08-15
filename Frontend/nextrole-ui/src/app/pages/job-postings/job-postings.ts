import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CurrencyPipe } from '@angular/common';

import { JobPostingService } from '../../services/jobPosting';
import { JobEntryService } from '../../services/jobEntry';
import { JobPostingResponse } from '../../models/job-posting.model';

@Component({
  selector: 'app-job-postings',
  standalone: true,
  imports: [CurrencyPipe],
  templateUrl: './job-postings.html',
  styleUrl: './job-postings.css',
})
export class JobPostings implements OnInit {
  private route = inject(ActivatedRoute);
  private postingService = inject(JobPostingService);
  private jobEntryService = inject(JobEntryService);

  postings = signal<JobPostingResponse[]>([]);
  isLoading = signal(false);
  searchQuery = signal('');
  expandedId = signal<number | null>(null);
  addedPostingIds = signal<Set<number>>(new Set());

  ngOnInit() {
    this.jobEntryService.getEntries().subscribe({
      next: (entries) => this.addedPostingIds.set(new Set(entries.map((entry) => entry.jobPostingId))),
      error: (err) => console.error('getEntries() failed:', err),
    });

    this.route.queryParamMap.subscribe((params) => {
      const q = params.get('q') ?? '';
      this.searchQuery.set(q);
      this.loadPostings(q);
    });
  }

  loadPostings(title: string) {
    this.isLoading.set(true);
    this.expandedId.set(null);

    this.postingService.searchByTitle(title).subscribe({
      next: (response) => {
        this.postings.set(response);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('searchByTitle() failed:', err);
        this.isLoading.set(false);
      },
    });
  }

  toggleExpanded(id: number) {
    this.expandedId.set(this.expandedId() === id ? null : id);
  }
}
