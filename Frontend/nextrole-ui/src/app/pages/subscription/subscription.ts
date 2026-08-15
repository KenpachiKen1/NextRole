import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

import { BillingService } from '../../services/billingService';
import { SubscriptionResponse } from '../../models/billing.model';
import { SubscriptionStatus } from '../../enums/subscription-status.enums';

import { Modal } from '../../components/global/modal/modal';

@Component({
  selector: 'app-subscription',
  standalone: true,
  imports: [ReactiveFormsModule, Modal],
  templateUrl: './subscription.html',
  styleUrl: './subscription.css',
})
export class Subscription implements OnInit {
  private billingService = inject(BillingService);
  private fb = inject(FormBuilder);

  SubscriptionStatus = SubscriptionStatus;

  subscription = signal<SubscriptionResponse | null>(null);

  isPremium = computed(() => {
    const status = this.subscription()?.subscriptionStatus;
    return status === SubscriptionStatus.SUBSCRIBED || status === SubscriptionStatus.TRIALING;
  });

  isBillingBusy = signal(false);
  billingError = signal('');

  showDowngradeModal = signal(false);

  downgradeForm = this.fb.nonNullable.group({
    comment: [''],
  });

  ngOnInit() {
    this.loadSubscription();
  }

  loadSubscription() {
    this.billingService.getSubscription().subscribe({
      next: (response) => this.subscription.set(response),
      error: (err) => console.error('Failed to load subscription:', err),
    });
  }

  upgrade() {
    this.isBillingBusy.set(true);
    this.billingError.set('');

    this.billingService.createCheckout().subscribe({
      next: (response) => {
        window.location.href = response.checkoutURL;
      },
      error: (err) => {
        console.error('Failed to start checkout:', err);
        this.isBillingBusy.set(false);
        this.billingError.set('Could not start checkout. Please try again.');
      },
    });
  }

  confirmDowngrade() {
    this.isBillingBusy.set(true);
    this.billingError.set('');

    this.billingService.cancelSubscription(this.downgradeForm.getRawValue()).subscribe({
      next: (response) => {
        this.subscription.set(response);
        this.isBillingBusy.set(false);
        this.showDowngradeModal.set(false);
        this.downgradeForm.reset({ comment: '' });
      },
      error: (err) => {
        console.error('Failed to downgrade subscription:', err);
        this.isBillingBusy.set(false);
        this.billingError.set('Could not downgrade your subscription. Please try again.');
      },
    });
  }
}
