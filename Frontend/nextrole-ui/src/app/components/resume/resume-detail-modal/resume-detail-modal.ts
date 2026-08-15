import { Component, Input, Output, EventEmitter } from '@angular/core';

import { ResumeResponse } from '../../../models/resume.model';

import { Modal } from '../../global/modal/modal';
import { Button } from '../../global/button/button';
import { ResumePreviewer } from '../resume-previewer/resume-previewer';
import { DatePipe } from '@angular/common';
import { DecimalPipe } from '@angular/common'; 

@Component({
  selector: 'app-resume-detail-modal',
  standalone: true,
  imports: [Modal, Button, ResumePreviewer, DatePipe, DecimalPipe],
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
  edit = new EventEmitter<ResumeResponse>();

  @Output()
  delete = new EventEmitter<number>();

  closeModal() {
    this.close.emit();
  }

  editResume() {
    this.edit.emit(this.resume);
  }

  deleteResume() {
    this.delete.emit(this.resume.id);
  }
}
