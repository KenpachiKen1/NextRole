import { AfterViewInit, Component, ElementRef, EventEmitter, HostListener, Input, Output, ViewChild, signal } from '@angular/core';

import { TERMS_OF_SERVICE_TEXT } from '../../../utilities/terms-content';
import { FocusTrapDirective } from '../../../directives/focus-trap';

@Component({
  selector: 'app-terms-modal',
  standalone: true,
  imports: [FocusTrapDirective],
  templateUrl: './terms-modal.html',
  styleUrl: './terms-modal.css',
})
export class TermsModal implements AfterViewInit {
  @Input() readOnly = false;
  @Output() agreed = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  @HostListener('document:keydown.escape')
  onEscape() {
    this.closed.emit();
  }

  @ViewChild('scrollContainer') scrollContainer!: ElementRef<HTMLDivElement>;

  termsText = TERMS_OF_SERVICE_TEXT;
  hasScrolledToEnd = signal(false);

  ngAfterViewInit() {
    const el = this.scrollContainer.nativeElement;
    if (el.scrollHeight <= el.clientHeight + 4) {
      this.hasScrolledToEnd.set(true);
    }
  }

  onScroll() {
    const el = this.scrollContainer.nativeElement;
    if (el.scrollTop + el.clientHeight >= el.scrollHeight - 4) {
      this.hasScrolledToEnd.set(true);
    }
  }

  onAgree() {
    if (this.hasScrolledToEnd()) {
      this.agreed.emit();
    }
  }
}
