export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
    lastName: string;
    username: String
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  userId: number;
    email: string;
    username:string
}
