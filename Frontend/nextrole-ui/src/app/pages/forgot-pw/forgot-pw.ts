import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/authService';
import { ForgotPasswordRequest } from '../../models/auth.models';

@Component({
  selector: 'app-forgot-pw',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './forgot-pw.html',
  styleUrl: './forgot-pw.css',
})
export class ForgotPw {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  submitted = signal(false);
  loading = signal(false);
  errorMessage = signal('');

  forgotForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  onSubmit() {
    if (this.forgotForm.invalid) return;

    this.loading.set(true);
    this.errorMessage.set('');

    const request: ForgotPasswordRequest = this.forgotForm.getRawValue();

    this.authService.forgotPassword(request).subscribe({
      next: () => {
        this.loading.set(false);
        this.submitted.set(true);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.error || 'Something went wrong. Please try again.');
      },
    });
  }

  goToReset() {
    const email = this.forgotForm.getRawValue().email;
    this.router.navigate(['/reset-password'], { queryParams: { email } });
  }
}
