import { Component } from '@angular/core';
import { Modal } from '../../global/modal/modal';
import { Button } from '../../global/button/button';
import { Inputs } from '../../global/input/input';
import { inject } from '@angular/core';
import { ResumeService } from '../../../services/resumeService';
import { FormBuilder, Validators } from '@angular/forms';

import { CreateResumeRequest } from '../../../models/resume.model';

import { EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-create-resume-modal',
  standalone: true,
  imports: [Modal, Button, Inputs],
  templateUrl: './create-resume-modal.html',
  styleUrl: './create-resume-modal.css',
})
export class CreateResumeModal {
  private resumeService = inject(ResumeService);
  private fb = inject(FormBuilder);

  @Output() close = new EventEmitter<void>();

  closeModal() {
    this.close.emit();
  }

  selectedFile: File | null = null;
  selectedFileName = '';

  isCreatingResume = false;
  createResumeError = false;
  createResumeErrorMessage = '';

  addResumeForm = this.fb.group({
    resumeTitle: ['', Validators.required],
    file: this.fb.control<File | null>(null, Validators.required),
  });

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      this.selectedFile = file;

      this.selectedFileName = file.name;

      // Remove extension
      const resumeName = file.name.replace(/\.[^/.]+$/, '').replace(' ', '_');

      this.addResumeForm.patchValue({
        file: file,
        resumeTitle: resumeName,
      });
    }
  }

  onSubmit() {
    if (this.addResumeForm.valid) {
      this.isCreatingResume = true;

      const resume: CreateResumeRequest = {
        file: this.addResumeForm.value.file!,
        resumeTitle: this.addResumeForm.value.resumeTitle!,
      };

      this.resumeService.createResume(resume).subscribe({
        next: () => {
          setTimeout(() => {
            //JUST FOR STATE TESTING REMOVE
            this.isCreatingResume = false;
          }, 3000);
        },

        error: (err) => {
          this.createResumeError = true;
          this.isCreatingResume = false;
          this.createResumeErrorMessage = 'Error occured while adding resume';
        },
      });
    }
  }
}