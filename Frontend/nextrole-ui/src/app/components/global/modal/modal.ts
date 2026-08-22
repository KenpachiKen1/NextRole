import { Component, EventEmitter, HostListener, Output } from '@angular/core';
import { FocusTrapDirective } from '../../../directives/focus-trap';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [FocusTrapDirective],
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

  @HostListener('document:keydown.escape')
  onEscape() {
    this.close.emit();
  }
}
