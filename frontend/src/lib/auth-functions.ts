import { api } from "./api-client";
import { AuthResponse } from "@/types/api";

export type LoginInput = {
  username: string;
  password: string;
};

export const validateLoginInput = (data: LoginInput): string[] => {
  const errors: string[] = [];

  if (!data.username) {
    errors.push("Email is required.");
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.username)) {
    errors.push("Email is invalid.");
  }

  if (!data.password || data.password.length < 6) {
    errors.push("Password must be at least 6 characters.");
  }

  return errors;
};

export const loginWithEmailAndPassword = (
  data: LoginInput,
): Promise<AuthResponse> => api.post("/auth/signin", data);

export type RegisterInput = {
  firstName: string;
  middleName?: string;
  lastName: string;
  email: string;
  password: string;
};

export const validateRegisterInput = (data: RegisterInput): string[] => {
  const errors: string[] = [];

  if (!data.firstName) errors.push("First name is required.");
  if (!data.lastName) errors.push("Last name is required.");

  if (!data.email) {
    errors.push("Email is required.");
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
    errors.push("Email is invalid.");
  }

  if (!data.password || data.password.length < 5) {
    errors.push("Password must be at least 5 characters.");
  }

  return errors;
};

export const registerWithEmailAndPassword = (
  data: RegisterInput,
): Promise<AuthResponse> => api.post("/auth/register", data);
