import { createBrowserRouter, RouterProvider, RouteObject } from "react-router";

import { paths } from "../config/paths";
import { ProtectedRoute } from "@/lib/auth";
import Home from "./routes/home";
import NotFoundRoute from "./routes/not-found";
import Dashboard from "./routes/app/dashboard";
import AppRoot, {
  ErrorBoundary as AppRootErrorBoundary,
} from "./routes/app/root";

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
          path: paths.app.dashboard.path,
          element: <Dashboard />,
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
