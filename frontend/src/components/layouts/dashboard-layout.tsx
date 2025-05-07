import DashboardTab from "@/features/dashboard/components/dashboard-tab";
import { useEffect, useState } from "react";
import { FiUser } from "react-icons/fi";
import { TbCash } from "react-icons/tb";
import { useLocation } from "react-router";

type LayoutProps = {
  children: React.ReactNode;
};

const DashboardLayout = ({ children }: LayoutProps) => {
  const location = useLocation();
  const [selectedTab, setSelectedTab] = useState("");

  useEffect(() => {
    const currentTab = location.pathname.split("/").at(-1);

    if (!currentTab) {
      setSelectedTab("members");
      return;
    }

    setSelectedTab(currentTab);
  }, [location.pathname]);

  return (
    <main className="grid grid-cols-[1fr_3fr_1fr] bg-base-100 *:size-full h-screen">
      <section className="p-8 w-full space-y-2 [&_button]:btn-full">
        <h2 className="text-2xl mb-20 font-semibold">Logo Here</h2>
        <select className="select select-lg !text-sm select-accent rounded-box bg-accent text-neutral-content mb-8">
          <option>UP Human Settlements</option>
          <option>YSES</option>
        </select>

        <DashboardTab
          leadingIcon={<FiUser size={16} />}
          label="Members"
          isSelected={"members" === selectedTab}
          updateTab={setSelectedTab}
        />
        <DashboardTab
          leadingIcon={<TbCash size={16} />}
          label="Fees"
          isSelected={"fees" === selectedTab}
          updateTab={setSelectedTab}
        />
      </section>
      <section className="bg-base-200 p-8">{children}</section>
      <section></section>
    </main>
  );
};

export default DashboardLayout;
