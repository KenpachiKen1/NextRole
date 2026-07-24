import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { JobPostingResponse, UpdateJobPostingRequest } from '../models/job-posting.model'

@Injectable({
  providedIn: 'root',
})
export class JobPostingService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/job-postings';

  private getHeaders() {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('access_token')}`,
    });
  }

  getAllJobPostings() {
    return this.http.get<JobPostingResponse[]>(`${this.apiUrl}/list-jobs/`, {
      headers: this.getHeaders(),
    });
  }

  getJobPostingById(id: number) {
    return this.http.get<JobPostingResponse>(`${this.apiUrl}/job/${id}/`, {
      headers: this.getHeaders(),
    });
  }

  getPostingsByCompany(companyId: number) {
    return this.http.get<JobPostingResponse[]>(`${this.apiUrl}/company/${companyId}`, {
      headers: this.getHeaders(),
    });
  }

  searchByTitle(title: string) {
    const params = new HttpParams().set('title', title);

    return this.http.get<JobPostingResponse[]>(`${this.apiUrl}/search`, {
      headers: this.getHeaders(),
      params,
    });
  }

  updateJobPosting(postingId: number, request: UpdateJobPostingRequest) {
    return this.http.put<JobPostingResponse>(`${this.apiUrl}/${postingId}`, request, {
      headers: this.getHeaders(),
    });
  }
}
