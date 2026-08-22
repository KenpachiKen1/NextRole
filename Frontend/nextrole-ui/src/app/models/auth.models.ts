export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
    lastName: string;
    username: String
  tosAccepted: boolean;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  email: string;
  code: string;
  newPassword: string;
}

export interface AuthResponse {
  token: string;
  userId: number;
    email: string;
    username:string
}
