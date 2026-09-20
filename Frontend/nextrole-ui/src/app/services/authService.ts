import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { RegisterRequest, LoginRequest, AuthResponse, ForgotPasswordRequest, ResetPasswordRequest } from '../models/auth.models';


//making it available everywhere
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/api/auth`;
    
    register(user: RegisterRequest) {

        console.log('Calling backend');

        return this.http.post<AuthResponse>(
            `${this.apiUrl}/register`,
            user
        )
    }

    login(user: LoginRequest) {
        return this.http.post<AuthResponse>(
            `${this.apiUrl}/login`,
            user
        )
    }

    forgotPassword(request: ForgotPasswordRequest) {
        return this.http.post(
            `${this.apiUrl}/forgot-password`,
            request,
            { responseType: 'text' }
        )
    }

    resetPassword(request: ResetPasswordRequest) {
        return this.http.put(
            `${this.apiUrl}/reset-password`,
            request,
            { responseType: 'text' }
        )
    }

}
