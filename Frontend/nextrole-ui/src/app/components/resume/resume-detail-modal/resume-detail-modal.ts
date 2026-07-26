import { Component, Input, Output, EventEmitter } from '@angular/core';

import { ResumeResponse } from '../../../models/resume.model';

import { Modal } from '../../global/modal/modal';
import { Button } from '../../global/button/button';
import { ResumePreviewer } from '../resume-previewer/resume-previewer';

@Component({
  selector: 'app-resume-detail-modal',
  standalone: true,
  imports: [Modal, Button, ResumePreviewer],
  templateUrl: './resume-detail-modal.html',
  styleUrl: './resume-detail-modal.css',
})
export class ResumeDetailModal {
  @Input()
  resume!: ResumeResponse;

  @Input()
  previewUrl: string | null = null;

  @Input()
  isLoading = false;

  @Output()
  close = new EventEmitter<void>();

  @Output()
  preview = new EventEmitter<ResumeResponse>();

  @Output()
  edit = new EventEmitter<ResumeResponse>();

  @Output()
  delete = new EventEmitter<number>();

  @Output()
  tailor = new EventEmitter<ResumeResponse>();

  closeModal() {
    this.close.emit();
  }

  previewResume() {
    this.preview.emit(this.resume);
  }

  editResume() {
    this.edit.emit(this.resume);
  }

  deleteResume() {
    this.delete.emit(this.resume.id);
  }

  tailorResume() {
    this.tailor.emit(this.resume);
  }
}
