import React from "react";

import { AuthProvider } from "@/lib/auth";

type AppProviderProps = {
  children: React.ReactNode;
};

export const AppProvider = ({ children }: AppProviderProps) => {
  return (
    <React.Suspense
      fallback={
        <div className="flex h-screen w-screen items-center justify-center">
          <div className="loading-spinner"></div>
        </div>
      }
    >
      <AuthProvider>{children}</AuthProvider>
    </React.Suspense>
  );
};
