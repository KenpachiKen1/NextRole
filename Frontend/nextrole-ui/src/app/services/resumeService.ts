
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CreateResumeRequest, UpdateResumeRequest, ViewSingleResumeResponse, ResumeResponse } from '../models/resume.model';
@Injectable({
  providedIn: 'root',
})
export class ResumeService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8000/api/resume';

  getHeaders() {
    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + localStorage.getItem('access_token'),
    });
    return headers;
  }
  createResume(resume: CreateResumeRequest) {
    const headers = this.getHeaders();
    const form = new FormData();
    form.append('file', resume.file);
    form.append('resumeTitle', resume.resumeTitle);
    return this.http.post<ResumeResponse>(`${this.apiUrl}/createResume`, form, {headers});
  }

  viewSingleResume(id: number) {
    const headers = this.getHeaders();
    return this.http.get<ViewSingleResumeResponse>
      (`${this.apiUrl}/showcaseResume/${id}/`,
        {
          headers: headers
        })
  }


  // change backend version to patch
  updateResume(id: number, request: UpdateResumeRequest) {
    const headers = this.getHeaders();
    return this.http.patch<ResumeResponse>(`${this.apiUrl}/updateResume/${id}/`, request, {
      headers: headers,
    });
  }

  resumeList() {
    const headers = this.getHeaders()
    return this.http.get<ResumeResponse[]>( //list of resume items
      `${this.apiUrl}/resume-list`,
      {
        headers: headers,
      },
    );
  }

  deleteResume(id: number) {
    const headers = this.getHeaders();
    return this.http.delete<ResumeResponse[]>(`${this.apiUrl}/deleteResume/${id}/`, {
      headers: headers,
    });
  }

  
}