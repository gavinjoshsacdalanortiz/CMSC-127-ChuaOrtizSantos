export const paths = {
  home: {
    path: "/",
    getHref: (redirectTo?: string | null | undefined) =>
      `/${redirectTo ? `?redirectTo=${encodeURIComponent(redirectTo)}` : ""}`,
  },
  app: {
    root: {
      path: "/app",
      getHref: () => "/",
    },
    dashboard: {
      path: "",
      getHref: () => "/app",
    },
  },
} as const;
