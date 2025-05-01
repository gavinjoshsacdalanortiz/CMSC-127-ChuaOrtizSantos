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
      root: {
        path: "/app/dashboard",
        getHref: () => "/app",
      },
      members: {
        path: "/app/dashboard/members",
        getHref: () => "/members",
      },
    },
  },
} as const;
