import React, {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  useEffect,
} from "react";
import { api } from "./api-client";
import { getToken, removeToken, setToken } from "@/lib/cookie";
import { AuthResponse } from "@/types/api";
import {
  LoginInput,
  RegisterInput,
  loginWithEmailAndPassword,
  registerWithEmailAndPassword,
} from "./auth-functions";
import { Navigate, useLocation } from "react-router";
import { paths } from "@/config/paths";
import { Member } from "@/types/member";

interface AuthContextType {
  member: Member | null;
  login: (data: LoginInput) => Promise<void>;
  register: (data: RegisterInput) => Promise<void>;
  logout: () => Promise<void>;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [member, setUser] = useState<Member | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchUser = useCallback(async () => {
    const token = getToken();
    if (!token) {
      setUser(null);
      setLoading(false);
      return;
    }
    try {
      const response = await api.get("/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUser(response);
    } catch (error) {
      console.error("Error fetching member:", error);
      removeToken();
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchUser();
  }, [fetchUser]);

  const login = useCallback(
    async (data: LoginInput) => {
      setLoading(true);
      try {
        const response: AuthResponse = await loginWithEmailAndPassword(data);
        setToken(response.token);
        getToken();
        await fetchUser();
      } catch (error) {
        console.error("Login failed:", error);
        setUser(null);
        throw error;
      } finally {
        setLoading(false);
      }
    },
    [fetchUser],
  );

  const register = useCallback(
    async (data: RegisterInput) => {
      setLoading(true);
      try {
        const response: AuthResponse = await registerWithEmailAndPassword(data);
        setToken(response.token);
        await fetchUser();
      } catch (error) {
        console.error("Registration failed", error);
        setUser(null);
        throw error;
      } finally {
        setLoading(false);
      }
    },
    [fetchUser],
  );

  const logout = useCallback(async () => {
    setLoading(true);
    try {
      await api.post("/auth/logout", {
        headers: { Authorization: `Bearer ${getToken()}` },
      });
      removeToken();
      setUser(null);
    } catch (error) {
      console.error("Logout failed", error);
      removeToken();
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  const authContextValue = useMemo(
    () => ({
      member,
      login,
      register,
      logout,
      loading,
    }),
    [member, login, register, logout, loading],
  );

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
};

export const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { member, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
      <div className="flex h-screen w-screen items-center justify-center">
        <span className="loading loading-xl loading-spinner text-neutral"></span>
      </div>
    );
  }

  if (!member) {
    return <Navigate to={paths.home.getHref()} replace />;
  }

  return children;
};
