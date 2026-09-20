import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {
  SubscriptionResponse,
  CancelSubscriptionRequest,
  CreateCheckoutSessionResponse,
} from '../models/billing.model';

import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class BillingService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/billing`;

  getHeaders() {
    return new HttpHeaders({
      Authorization: 'Bearer ' + localStorage.getItem('access_token'),
    });
  }

  getSubscription() {
    const headers = this.getHeaders();
    return this.http.get<SubscriptionResponse>(`${this.apiUrl}/subscription`, { headers });
  }

  createCheckout() {
    const headers = this.getHeaders();
    return this.http.post<CreateCheckoutSessionResponse>(`${this.apiUrl}/checkout`, {}, { headers });
  }

  cancelSubscription(request: CancelSubscriptionRequest) {
    const headers = this.getHeaders();
    return this.http.post<SubscriptionResponse>(`${this.apiUrl}/cancel`, request, { headers });
  }
}
