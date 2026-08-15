import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { UserService } from '../../services/userService';
import { BillingService } from '../../services/billingService';
import { UpdateUserRequest } from '../../models/user.model';
import { SubscriptionResponse } from '../../models/billing.model';
import { SubscriptionStatus } from '../../enums/subscription-status.enums';
import { SubscriptionStatusInfo } from '../../utilities/subscription-status-lookup';

import { Modal } from '../../components/global/modal/modal';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ReactiveFormsModule, DatePipe, Modal],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  private userService = inject(UserService);
  private billingService = inject(BillingService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  currentUser = this.userService.currentUser;
  subscription = signal<SubscriptionResponse | null>(null);
  subscriptionStatusInfo = SubscriptionStatusInfo;
  SubscriptionStatus = SubscriptionStatus;

  isSavingProfile = signal(false);
  profileSaved = signal(false);
  profileError = signal('');

  isBillingBusy = signal(false);
  billingError = signal('');

  showCancelModal = signal(false);
  showDeleteModal = signal(false);
  isDeletingAccount = signal(false);
  deleteError = signal('');

  issueSent = signal(false);

  profileForm = this.fb.nonNullable.group({
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
  });

  cancelForm = this.fb.nonNullable.group({
    comment: [''],
  });

  issueForm = this.fb.nonNullable.group({
    subject: ['', Validators.required],
    description: ['', Validators.required],
  });

  ngOnInit() {
    this.userService.getCurrUserProfile().subscribe({
      next: (user) => {
        this.currentUser.set(user);
        this.profileForm.patchValue({
          username: user.username,
          email: user.email,
        });
      },
      error: (err) => console.error('Failed to load profile:', err),
    });

    this.loadSubscription();
  }

  loadSubscription() {
    this.billingService.getSubscription().subscribe({
      next: (response) => this.subscription.set(response),
      error: (err) => console.error('Failed to load subscription:', err),
    });
  }

  saveProfile() {
    if (this.profileForm.invalid) {
      return;
    }

    this.isSavingProfile.set(true);
    this.profileSaved.set(false);
    this.profileError.set('');

    const request: UpdateUserRequest = { ...this.profileForm.getRawValue(), profilePhoto: '' };

    this.userService.updateUser(request).subscribe({
      next: (response) => {
        this.currentUser.set(response);
        this.isSavingProfile.set(false);
        this.profileSaved.set(true);
      },
      error: (err) => {
        console.error('Failed to update profile:', err);
        this.isSavingProfile.set(false);
        this.profileError.set('Something went wrong while saving your changes.');
      },
    });
  }

  upgradeToPremium() {
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

  confirmCancelSubscription() {
    this.isBillingBusy.set(true);
    this.billingError.set('');

    this.billingService.cancelSubscription(this.cancelForm.getRawValue()).subscribe({
      next: (response) => {
        this.subscription.set(response);
        this.isBillingBusy.set(false);
        this.showCancelModal.set(false);
        this.cancelForm.reset({ comment: '' });
      },
      error: (err) => {
        console.error('Failed to cancel subscription:', err);
        this.isBillingBusy.set(false);
        this.billingError.set('Could not cancel your subscription. Please try again.');
      },
    });
  }

  confirmDeleteAccount() {
    this.isDeletingAccount.set(true);
    this.deleteError.set('');

    this.userService.deleteAccount().subscribe({
      next: () => {
        localStorage.removeItem('access_token');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Failed to delete account:', err);
        this.isDeletingAccount.set(false);
        this.deleteError.set('Could not delete your account. Please try again.');
      },
    });
  }

  logout() {
    localStorage.removeItem('access_token');
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  sendIssueReport() {
    if (this.issueForm.invalid) {
      return;
    }

    const { subject, description } = this.issueForm.getRawValue();
    const email = this.currentUser()?.email ?? '';
    const body = `${description}\n\n---\nReported by: ${email}`;

    window.location.href = `mailto:support@nextrole.app?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;

    this.issueSent.set(true);
    this.issueForm.reset({ subject: '', description: '' });
  }
}
