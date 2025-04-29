export type BaseEntity = {
  id: string;
  createdAt: number;
};

export type Entity<T> = {
  [K in keyof T]: T[K];
} & BaseEntity;

export type User = Entity<{
  firstName: string;
  middleName: string;
  lastName: string;
  email: string;
  role: "ADMIN" | "USER";
  cart: { [key: string]: number };
}>;

export type AuthResponse = {
  token: string;
};
