import { Component } from '@angular/core';
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [],
  templateUrl: './modal.html',
  styleUrl: './modal.css',
})
export class Modal {
  @Output() close = new EventEmitter<void>();

  isVisible = false;

  ngOnInit() {
    setTimeout(() => {
      this.isVisible = true;
    });
  }
}
