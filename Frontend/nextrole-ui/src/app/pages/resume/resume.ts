import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { ResumeService } from '../../services/resumeService';

import { Button } from '../../components/global/button/button';
import { CreateResumeModal } from '../../components/resume/create-resume-modal/create-resume-modal';
import { ResumeCard } from '../../components/resume/resume-card/resume-card';
import { ResumeDetailModal } from '../../components/resume/resume-detail-modal/resume-detail-modal';

import { ResumeResponse, UpdateResumeRequest } from '../../models/resume.model';

@Component({
  selector: 'app-resume',
  imports: [Button, CreateResumeModal, ResumeCard, ResumeDetailModal],
  templateUrl: './resume.html',
  styleUrl: './resume.css',
})
export class Resume implements OnInit {
  private resumeService = inject(ResumeService);
  private fb = inject(FormBuilder);

  user_resume_list = signal<ResumeResponse[]>([]);

  selectedResumeForModal: ResumeResponse | null = null;

  selectedResumeId!: number;

  previewUrl = signal<string | null>(null);

  isLoadingPreview = signal(false);

  showCreateResumeModal = false;

  updateResumeForm = this.fb.nonNullable.group({
    resumeTitle: ['', Validators.required],
  });

  ngOnInit() {
    this.loadResumes();
  }

  loadResumes() {
    this.resumeService.resumeList().subscribe({
      next: (response) => {
        this.user_resume_list.set(Array.isArray(response) ? response : [response]);
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  openResumeDetail(resume: ResumeResponse) {
    this.selectedResumeForModal = resume;

    this.previewResume(resume);
  }

  closeResumeDetail() {
    this.selectedResumeForModal = null;

    this.previewUrl.set(null);

    this.isLoadingPreview.set(false);
  }

  previewResume(resume: ResumeResponse) {
    console.log('Loading preview for:', resume.id);

    this.isLoadingPreview.set(true);
    this.previewUrl.set(null);

    this.resumeService.viewSingleResume(resume.id).subscribe({
      next: (response) => {
        console.log('Resume response:', response);

        this.previewUrl.set(response.url);

        this.isLoadingPreview.set(false);
      },

      error: (err) => {
        console.error('Preview failed:', err);

        this.isLoadingPreview.set(false);
      },
    });
  }

  handleEditFromModal(resume: ResumeResponse) {
    this.openEditResume(resume);

    this.closeResumeDetail();
  }

  openEditResume(resume: ResumeResponse) {
    this.selectedResumeId = resume.id;

    this.updateResumeForm.patchValue({
      resumeTitle: resume.resumeTitle,
    });
  }

  handleDeleteFromModal(resumeId: number) {
    this.selectedResumeId = resumeId;

    this.deleteResume();
  }

  deleteResume() {
    this.resumeService.deleteResume(this.selectedResumeId).subscribe({
      next: (response) => {
        this.user_resume_list.set(response);
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  handleTailorFromModal(resume: ResumeResponse) {
    console.log('Tailor requested:', resume.resumeTitle);
  }

  updateResume() {
    if (this.updateResumeForm.valid) {
      const request: UpdateResumeRequest = this.updateResumeForm.getRawValue();

      this.resumeService.updateResume(this.selectedResumeId, request).subscribe({
        next: () => {
          this.loadResumes();
        },

        error: (err) => {
          console.error(err);
        },
      });
    }
  }
}
