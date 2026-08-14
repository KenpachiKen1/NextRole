import { SubscriptionStatus } from '../enums/subscription-status.enums';

export const SubscriptionStatusInfo = {
  [SubscriptionStatus.FREE]: {
    label: 'Free Account',
    description: 'You are currently on the free plan',
    color: '#9E9E9E',
  },

  [SubscriptionStatus.SUBSCRIBED]: {
    label: 'Premium Account',
    description: 'You are currently subscribed to NextRole Premium',
    color: '#B9932A',
  },

  [SubscriptionStatus.PENDING]: {
    label: 'Pending',
    description: 'Your subscription is being processed',
    color: '#FF9800',
  },

  [SubscriptionStatus.TRIALING]: {
    label: 'Trial Account',
    description: 'You are currently on a free trial of NextRole Premium',
    color: '#4CAF50',
  },

  [SubscriptionStatus.CANCELED]: {
    label: 'Canceled',
    description: 'Your subscription has been canceled',
    color: '#757575',
  },

  [SubscriptionStatus.PAST_DUE]: {
    label: 'Past Due',
    description: 'Your payment is past due. Please update your billing information',
    color: '#F44336',
  },
} as const;
