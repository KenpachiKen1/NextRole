import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { RegisterRequest, LoginRequest, AuthResponse } from '../models/auth.models';


//making it available everywhere
@Injectable({
  providedIn: 'root',
})
export class AuthService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:8080/api/auth';
    
    register(user: RegisterRequest) {
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

}
