import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { FeedbackRequest } from '../models/feedback.models';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class Dynamo {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/api/aws-tools`;
  private getHeaders() {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('access_token')}`,
    });
  }
    sendFeedback(feedbackRequest: FeedbackRequest) {
    return this.http.post(`${this.apiUrl}/sendFeedback`, feedbackRequest, {
      headers: this.getHeaders(),
      responseType: 'text',
    });
  }
}