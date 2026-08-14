import { SubscriptionStatus } from '../enums/subscription-status.enums';

export interface SubscriptionResponse {
  subscriptionStatus: SubscriptionStatus;
  planName: string;
  currentPeriodEnd: string | null;
  cancelAtPeriodEnd: boolean;
}

export interface CancelSubscriptionRequest {
  comment: string;
}

export interface CreateCheckoutSessionResponse {
  checkoutURL: string;
}
