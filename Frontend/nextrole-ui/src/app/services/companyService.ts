import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { CompanyResponse } from '../models/company.model';
import { JobPostingResponse } from '../models/job-posting.model';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/companies';

  private getHeaders() {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('access_token')}`,
    });
  }

  getCompanies() {
    return this.http.get<CompanyResponse[]>(`${this.apiUrl}/list`, { headers: this.getHeaders() });
  }

  getCompanyById(id: number) {
    return this.http.get<CompanyResponse>(`${this.apiUrl}/${id}/`, { headers: this.getHeaders() });
  }


  search(company: string) {

    const params = new HttpParams().set('name', company);

    return this.http.get<CompanyResponse>(`${this.apiUrl}/search`, {
      headers: this.getHeaders(),
      params
    })
  }

  getCompanyJobPostings(id: number) {
    return this.http.get<JobPostingResponse[]>(`${this.apiUrl}/${id}/job-postings`, {
      headers: this.getHeaders(),
    });
  }
}
