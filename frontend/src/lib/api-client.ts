import { env } from "@/config/env";
import { paths } from "@/config/paths";
import { getToken } from "./cookie";

type FetchOptions = Omit<RequestInit, "body"> & {
  params?: Record<string, any>;
};

const constructUrlWithParams = (
  baseUrl: string,
  params?: Record<string, any>,
) => {
  if (!params || Object.keys(params).length === 0) {
    return baseUrl;
  }

  const validParams: Record<string, string> = {};
  for (const key in params) {
    if (
      Object.prototype.hasOwnProperty.call(params, key) &&
      params[key] !== undefined &&
      params[key] !== null
    ) {
      validParams[key] = String(params[key]);
    }
  }

  const searchParams = new URLSearchParams(validParams).toString();

  if (!searchParams) {
    return baseUrl;
  }

  const separator = baseUrl.includes("?") ? "&" : "?";

  return `${baseUrl}${separator}${searchParams}`;
};

const handleUnauthorized = () => {
  const redirectTo = window.location.pathname;
  window.location.href = paths.home.getHref(redirectTo);
};

const defaultHeaders = {
  Accept: "application/json",
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
};

const fetchWithAuth = async <T = any>(
  url: string,
  options: RequestInit = {},
): Promise<T> => {
  const config: RequestInit = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...(options.headers || {}),
    },
    credentials: "include",
  };

  const res = await fetch(`${env.API_URL}${url}`, config);

  if (!res.ok) {
    const errorBody = await res.json().catch(() => ({}));
    return Promise.reject({
      status: res.status,
      message: errorBody.message || "Request failed",
      error: errorBody,
    });
  }

  return res.json();
};

export const api = {
  get: <T = any>(url: string, options?: FetchOptions) => {
    const { params, ...fetchOptions } = options || {};
    const finalUrl = constructUrlWithParams(url, params);
    return fetchWithAuth<T>(finalUrl, { ...fetchOptions, method: "GET" });
  },
  post: <T = any>(url: string, data?: any, options?: RequestInit) =>
    fetchWithAuth<T>(url, {
      ...options,
      method: "POST",
      body: JSON.stringify(data),
    }),
  put: <T = any>(url: string, data?: any, options?: RequestInit) =>
    fetchWithAuth<T>(url, {
      ...options,
      method: "PUT",
      body: JSON.stringify(data),
    }),
  delete: <T = any>(url: string, options?: RequestInit) =>
    fetchWithAuth<T>(url, { ...options, method: "DELETE" }),
};
