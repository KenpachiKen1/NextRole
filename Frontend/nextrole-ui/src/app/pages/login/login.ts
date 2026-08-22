import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/authService';
import { LoginRequest } from '../../models/auth.models';
import { TermsModal } from '../../components/global/terms-modal/terms-modal';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, TermsModal],
  templateUrl: './login.html',
  styleUrl: './login.css',
})


export class Login {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder)
  private router = inject(Router);

  showTermsModal = signal(false);
  errorMessage = signal('');
  loading = signal(false);

  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  })

  onSubmit() {
    if (this.loginForm.valid) {
      const user: LoginRequest = this.loginForm.getRawValue();

      this.errorMessage.set('');
      this.loading.set(true);

      this.authService.login(user).subscribe({
        next: (response) => {
          localStorage.setItem('access_token', response.token);
          this.router.navigate(['/calendar']);
        },

        error: (err) => {
          console.log('login failed', err)
          this.loading.set(false);
          this.errorMessage.set('Incorrect email or password. Please try again.');
        }
      })
    }
  }
}
