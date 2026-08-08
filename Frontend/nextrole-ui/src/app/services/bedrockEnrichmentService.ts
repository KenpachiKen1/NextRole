import { Injectable, inject } from "@angular/core";
import { InterviewPrepResponse, ResumeTailoringResponse, ResumeFeedbackResponse } from "../models/bedrockAgents.model";

import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
@Injectable({
  providedIn: 'root',
})
export class bedrockEnrichmentService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/aws-tools';
  private getHeaders() {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('access_token')}`,
    });
  }
  tailorResume(resumeId: number | null, jobPostingId: number | null) {
    return this.http.post<ResumeTailoringResponse>(
      `${this.apiUrl}/resumeTailoring-agent`,
      { resumeId, jobPostingId },
      { headers: this.getHeaders() },
    );
  }

    InvokeInterviewPrep(resumeId: number, jobPostingId: number) { 
         return this.http.post<InterviewPrepResponse>(
           `${this.apiUrl}/interviewPrep-agent`,
           { resumeId, jobPostingId },
           { headers: this.getHeaders() },
         );
    }


    resumeFeedback(resumeId: number) {
        return this.http.post<ResumeFeedbackResponse>(
          `${this.apiUrl}/resumeFeedback-agent`,
          { resumeId },
          { headers: this.getHeaders() },
        );
    }
    
}