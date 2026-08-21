import { Component, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/authService';
import { ResetPasswordRequest } from '../../models/auth.models';

function passwordsMatch(group: AbstractControl): ValidationErrors | null {
  const newPassword = group.get('newPassword')?.value;
  const confirmPassword = group.get('confirmPassword')?.value;
  return newPassword === confirmPassword ? null : { passwordMismatch: true };
}

@Component({
  selector: 'app-reset-pw',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './reset-pw.html',
  styleUrl: './reset-pw.css',
})
export class ResetPw {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  loading = signal(false);
  success = signal(false);
  errorMessage = signal('');

  resetForm = this.fb.nonNullable.group(
    {
      email: ['', [Validators.required, Validators.email]],
      code: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(3)]],
      confirmPassword: ['', Validators.required],
    },
    { validators: passwordsMatch },
  );

  constructor() {
    const email = this.route.snapshot.queryParamMap.get('email');
    if (email) {
      this.resetForm.patchValue({ email });
    }
  }

  onSubmit() {
    if (this.resetForm.invalid) return;

    this.loading.set(true);
    this.errorMessage.set('');

    const { email, code, newPassword } = this.resetForm.getRawValue();
    const request: ResetPasswordRequest = { email, code, newPassword };

    this.authService.resetPassword(request).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set(true);
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.error || 'This code is invalid or has expired.');
      },
    });
  }
}
