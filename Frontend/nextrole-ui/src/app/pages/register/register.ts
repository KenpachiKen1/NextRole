import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/authService';
import { RegisterRequest } from '../../models/auth.models';
import { TermsModal } from '../../components/global/terms-modal/terms-modal';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, TermsModal],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  showTermsModal = signal(false);
  errorMessage = signal('');
  loading = signal(false);

  signupForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
    username: ['', Validators.required],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    tosAccepted: [false, Validators.requiredTrue],
  });

  onTermsAgreed() {
    this.signupForm.patchValue({ tosAccepted: true });
    this.showTermsModal.set(false);
  }

  onSubmit() {
     console.log('REGISTER CLICKED');
    if (this.signupForm.valid) {
        console.log('FORM VALID');

      const user: RegisterRequest = this.signupForm.getRawValue();

      this.errorMessage.set('');
      this.loading.set(true);

      this.authService.register(user).subscribe({


        next: (response) => {
          localStorage.setItem("access_token", response.token)
          this.router.navigate(['/calendar']);
        },

        error: (err) => {
          console.log('Registration failed', err);
          this.loading.set(false);
          this.errorMessage.set('We couldn\'t create your account. The email or username may already be in use.');
        },
      });
    }
  }
}
