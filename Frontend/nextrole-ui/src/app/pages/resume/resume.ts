import { Component, inject} from '@angular/core';
import { ResumeService } from '../../services/resumeService';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Button } from '../../components/global/button/button';
import { CreateResumeModal } from '../../components/resume/create-resume-modal/create-resume-modal';
import { ResumeResponse, ViewSingleResumeResponse, UpdateResumeRequest, CreateResumeRequest } from '../../models/resume.model';
@Component({
  selector: 'app-resume',
  imports: [Button, CreateResumeModal],
  templateUrl: './resume.html',
  styleUrl: './resume.css',
})
export class Resume {
  private resumeService = inject(ResumeService);
  private fb = inject(FormBuilder);

  selectedResumeId!: number;
  selectedResume!: ViewSingleResumeResponse;
  user_resume_list!: ResumeResponse[];

  selectedFileName = '';
  isCreatingResume = false;
  createResumeError = '';

  showCreateResumeModal = false;

  openEditResume(resume: ResumeResponse) {
    this.selectedResumeId = resume.id;
    this.updateResumeForm.patchValue({
      resumeTitle: resume.resumeTitle,
    });
  }

  //would be the modal confirmation button
  openDeleteResume(resume: ResumeResponse) {
    this.selectedResumeId = resume.id;
  }
  addResumeForm = this.fb.group({
    resumeTitle: [this.fb.nonNullable.control('', Validators.required)],
    file: this.fb.control<File | null>(null, Validators.required),
  });

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.addResumeForm.patchValue({
        file: input.files[0],
      });
      this.selectedFileName = file.name;
    }
  }

  updateResumeForm = this.fb.nonNullable.group({
    resumeTitle: ['', Validators.required],
  });

  updateResume() {
    if (this.updateResumeForm.valid) {
      const update: UpdateResumeRequest = this.updateResumeForm.getRawValue();
      this.resumeService.updateResume(this.selectedResumeId, update).subscribe({
        next: (response) => {
          this.updateResumeForm.patchValue({
            resumeTitle: response.resumeTitle,
          });

          this.updateResumeForm.markAsPristine();
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }

  viewSingleResume(resume: ResumeResponse) {
    this.selectedResumeId = resume.id;
  }

  resumeDetails() {
    this.resumeService.viewSingleResume(this.selectedResumeId).subscribe({
      next: (response) => {
        this.selectedResume = response;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  deleteResume() {
    this.resumeService.deleteResume(this.selectedResumeId).subscribe({
      next: (response) => {
        this.user_resume_list = response; //updated resume-list
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
