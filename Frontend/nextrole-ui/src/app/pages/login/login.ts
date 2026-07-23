import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/authService';
import { LoginRequest } from '../../models/auth.models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
  
  
export class Login {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder)

  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  })

  onSubmit() {
    if (this.loginForm.valid) {
      const user: LoginRequest = this.loginForm.getRawValue();

      this.authService.login(user).subscribe({
        next: (response) => {
          console.log('login successful', response)
        },

        error: (err) => {
          console.log('login failed', err)
        }
      })
    }
  }
}
