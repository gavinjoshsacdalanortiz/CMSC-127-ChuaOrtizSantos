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
        path: "/app/dashboard/:orgId/members",
        getHref: (id: string) => `/app/dashboard/${id}/members`,
      },
      fees: {
        path: "/app/dashboard/:orgId/fees",
        getHref: (id: string) => `/app/dashboard/${id}/fees`,
      },
      manageFees: {
        path: "/app/dashboard/:orgId/manage",
        getHref: (id: string) => `/app/dashboard/${id}/manage`,
      },
    },
  },
} as const;
