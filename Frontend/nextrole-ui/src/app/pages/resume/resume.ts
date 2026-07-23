import { Component, inject} from '@angular/core';
import { ResumeService } from '../../services/resumeService';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ResumeResponse, ViewSingleResumeResponse, UpdateResumeRequest, CreateResumeRequest } from '../../models/resume.model';
@Component({
  selector: 'app-resume',
  imports: [],
  templateUrl: './resume.html',
  styleUrl: './resume.css',
})
export class Resume {
  private resumeService = inject(ResumeService);
  private fb = inject(FormBuilder);

  selectedResumeId!: number;

  openEditResume(resume: ResumeResponse) {
    this.selectedResumeId = resume.id;
    this.updateResumeForm.patchValue({
      resumeTitle: resume.resumeTitle
    })
  }

  openDeleteResume(resume: ResumeResponse) {
    
  }
  addResumeForm = this.fb.group({
    resumeTitle: [this.fb.nonNullable.control('', Validators.required)],
    file: this.fb.control<File | null>(null, Validators.required),
  });

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.addResumeForm.patchValue({
        file: input.files[0],
      });
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
          console.log(response);
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

  deleteResume() {
    
  }
  onSubmit() {
    if (this.addResumeForm.valid) {
      const resume: CreateResumeRequest = {
        file: this.addResumeForm.value.file!,
        resumeTitle: this.addResumeForm.value.resumeTitle!,
      };

      this.resumeService.createResume(resume).subscribe({
        next: (response) => {
          console.log(response);
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }
}
