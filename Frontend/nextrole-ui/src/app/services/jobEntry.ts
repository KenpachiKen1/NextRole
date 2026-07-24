import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { CreateJobEntryRequest, JobEntryResponse, UpdateJobEntryRequest,} from '../models/job-entry.model';
import { JobStatus } from '../enums/jobEntry-status.enums'

@Injectable({
  providedIn: 'root',
})
export class JobEntryService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/jobEntry';

  private getHeaders() {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('access_token')}`,
    });
  }

  getEntry(id: number) {
    return this.http.get<JobEntryResponse>(`${this.apiUrl}/getEntry/${id}`, {
      headers: this.getHeaders(),
    });
  }

  createEntry(jobPostingID: number, resumeId: number, request: CreateJobEntryRequest) {
    return this.http.post<JobEntryResponse>(
      `${this.apiUrl}/create-entry/${jobPostingID}/${resumeId}/`,
      request,
      {
        headers: this.getHeaders(),
      },
    );
  }

  updateEntry(entryId: number, request: UpdateJobEntryRequest) {
    return this.http.put<JobEntryResponse>(`${this.apiUrl}/updateJobEntry/${entryId}`, request, {
      headers: this.getHeaders(),
    });
  }

  deleteEntry(postingId: number) {
    return this.http.delete<JobEntryResponse[]>(`${this.apiUrl}/deleteEntry/${postingId}`, {
      headers: this.getHeaders(),
    });
  }

  getEntries(status?: JobStatus, title?: string) {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }

    if (title) {
      params = params.set('title', title);
    }

    return this.http.get<JobEntryResponse[]>(`${this.apiUrl}/jobEntryList`, {
      headers: this.getHeaders(),
      params,
    });
  }
}
