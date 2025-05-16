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
import { AuthResponse, User } from "@/types/api";
import {
  LoginInput,
  RegisterInput,
  loginWithEmailAndPassword,
  registerWithEmailAndPassword,
} from "./auth-functions";
import { Navigate, useLocation } from "react-router";
import { paths } from "@/config/paths";

interface AuthContextType {
  user: User | null;
  login: (data: LoginInput) => Promise<void>;
  register: (data: RegisterInput) => Promise<void>;
  logout: () => Promise<void>;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
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
      console.error("Error fetching user:", error);
      // removeToken();
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
        await fetchUser();
      } catch (error) {
        console.error("Login failed:", error);
        setUser(null);
        throw error;
      } finally {
        setLoading(false);
      }
    },
    [fetchUser]
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
    [fetchUser]
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
      user,
      login,
      register,
      logout,
      loading,
    }),
    [user, login, register, logout, loading]
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
  const { user, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!user) {
    return <Navigate to={paths.home.getHref(location.pathname)} replace />;
  }

  return children;
};
