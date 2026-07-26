import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/authService';
import { RegisterRequest } from '../../models/auth.models';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);

  signupForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
    username: ['', Validators.required],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
  });

  onSubmit() {
     console.log('REGISTER CLICKED');
    if (this.signupForm.valid) {
        console.log('FORM VALID');

      const user: RegisterRequest = this.signupForm.getRawValue();

      this.authService.register(user).subscribe({


        next: (response) => {
          localStorage.setItem("access_token", response.token)
        },

        error: (err) => {
          console.log('Registration failed', err);
        },
      });
    }
  }
}
