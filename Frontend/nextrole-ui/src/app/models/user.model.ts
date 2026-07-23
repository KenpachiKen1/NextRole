import { SubscriptionStatus } from '../enums/subscription-status.enums';

export interface UserResponse {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  status: SubscriptionStatus;
}

export interface UpdateUserRequest{
    username: string,
    profilePhoto: string,
    email: string
}


