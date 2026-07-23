import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserResponse, UpdateUserRequest } from '../models/user.model';


@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/user';

    
    getHeaders() {
        const headers = new HttpHeaders({
            "Authorization": "Bearer " + localStorage.getItem("access_token")
        })
        return headers
    }
    updateUser(user: UpdateUserRequest) {
        const headers = this.getHeaders();
      this.http.post<UserResponse>(
          `${this.apiUrl}/updateAccount`,user, {headers});
  }

    getCurrUserProfile() {
       const headers = this.getHeaders();
    this.http.post<UserResponse>(`${this.apiUrl}/profile`, {headers});
    }

    deleteAccount() {
        const headers = this.getHeaders();
        this.http.delete(`${this.apiUrl}/deleteAccount`, {headers});
    }
    

}