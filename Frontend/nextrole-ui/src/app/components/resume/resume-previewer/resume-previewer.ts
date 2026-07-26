import { Component, Input, inject } from '@angular/core';

import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-resume-previewer',
  standalone: true,
  templateUrl: './resume-previewer.html',
  styleUrl: './resume-previewer.css',
})
export class ResumePreviewer {
  private sanitizer = inject(DomSanitizer);

  safePreviewUrl: SafeResourceUrl | null = null;

  @Input()
  isLoading = false;

  @Input()
  set previewUrl(url: string | null) {
    if (url) {
      this.safePreviewUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    } else {
      this.safePreviewUrl = null;
    }
  }
}
