import DashboardLayout from "@/components/layouts/dashboard-layout";
import { Outlet } from "react-router";

const DashboardRoot = () => {
  return (
    <DashboardLayout>
      <Outlet />
    </DashboardLayout>
  );
};

export default DashboardRoot;
