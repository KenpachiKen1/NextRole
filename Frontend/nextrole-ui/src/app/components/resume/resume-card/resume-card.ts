import { ResumeResponse } from '../../../models/resume.model';
import { Component, EventEmitter, Input, Output, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-resume-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './resume-card.html',
  styleUrl: './resume-card.css',
})
export class ResumeCard implements OnChanges {
  @Input() resume!: ResumeResponse;

  @Output() selected = new EventEmitter<ResumeResponse>();

  ngOnChanges() {
    console.log('Resume card received:', this.resume);
  }

  openResume() {
    this.selected.emit(this.resume);
  }
}
