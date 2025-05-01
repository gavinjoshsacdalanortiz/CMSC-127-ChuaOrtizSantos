import { createBrowserRouter, RouterProvider, RouteObject } from "react-router";

import { paths } from "../config/paths";
import { ProtectedRoute } from "@/lib/auth";
import Home from "./routes/home";
import NotFoundRoute from "./routes/not-found";
import AppRoot, {
  ErrorBoundary as AppRootErrorBoundary,
} from "./routes/app/root";
import MembersDashboard from "./routes/app/dashboard/members";
import DashboardRoot from "./routes/app/dashboard/root";

const createAppRouter = () => {
  const routes: RouteObject[] = [
    {
      path: paths.home.path,
      element: <Home />,
    },
    {
      path: paths.app.root.path,
      element: <AppRoot />,
      ErrorBoundary: AppRootErrorBoundary,
      children: [
        {
          path: paths.app.dashboard.root.path,
          element: <DashboardRoot />,
          children: [
            {
              path: paths.app.dashboard.members.path,
              element: <MembersDashboard />,
            },
          ],
        },
        // TODO:dashboard, etc...
      ],
    },
    {
      path: "*",
      element: <NotFoundRoute />,
    },
  ];
  return createBrowserRouter(routes);
};

export const AppRouter = () => {
  const router = createAppRouter();

  return <RouterProvider router={router} />;
};
