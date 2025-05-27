import { paths } from "@/config/paths";
import DashboardTab from "@/features/dashboard/components/dashboard-tab";
import { useAuth } from "@/lib/auth";
import { useEffect, useState } from "react";
import { FiBriefcase, FiUser } from "react-icons/fi";
import { TbCash } from "react-icons/tb";
import { useLocation, useNavigate } from "react-router"; // useLocation, useNavigate from react-router-dom

type LayoutProps = {
  children: React.ReactNode;
};

const DashboardLayout = ({ children }: LayoutProps) => {
  const { member } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [selectedTab, setSelectedTab] = useState("members"); // Default to 'members' initially
  const [selectedOrganization, setSelectedOrganization] = useState("");

  const isOrgSelectionDisabled =
    !member || Object.keys(member?.organizationRolesMap || {}).length === 0;

  useEffect(() => {
    if (member && member.organizationRolesMap && !selectedOrganization) {
      const organizationIds = Object.keys(member.organizationRolesMap);
      if (organizationIds.length > 0) {
        const defaultOrgId = organizationIds[0];
        setSelectedOrganization(defaultOrgId);
      }
    }
  }, [member, selectedOrganization]);

  useEffect(() => {
    if (selectedOrganization) {
      console.log("Current pathname:", location.pathname);
      const pathSegments = location.pathname.split("/");
      // e.g., ["", "app", "dashboard", "orgId", "members"]
      // or ["", "app", "dashboard", "orgId"]

      // Let's assume your base organization URL is /app/dashboard/:orgId
      // and tabs are /app/dashboard/:orgId/members or /app/dashboard/:orgId/fees
      // So, currentTabRoute would be pathSegments[4] if it exists
      // And the organization ID would be pathSegments[3]

      const currentTabRoute = pathSegments[4]; // More robustly, find based on known structure
      // Or, if your URL structure is /dashboard/:tab/:orgId
      // const currentTabRoute = pathSegments[2];

      if (currentTabRoute && ["members", "fees"].includes(currentTabRoute)) {
        switch (
          selectedTab // selectedTab comes from your component's state
        ) {
          case "members":
            navigate(paths.app.dashboard.members.getHref(selectedOrganization));
            break;
          case "fees":
            navigate(paths.app.dashboard.fees.getHref(selectedOrganization));
            break;
          default:
            console.warn(
              "Selected tab is not recognized, navigating to default members tab.",
            );
            navigate(paths.app.dashboard.members.getHref(selectedOrganization));
            break;
        }
      } else {
        console.log(
          "No recognized tab, navigating to default members tab for new org.",
        );
        navigate(paths.app.dashboard.members.getHref(selectedOrganization));
      }
    }
  }, [selectedOrganization, selectedTab, location.pathname, navigate, paths]); // Add all dependencies

  useEffect(() => {
    const pathSegments = location.pathname.split("/");
    const currentPotentialTab = pathSegments.pop();
    const currentPotentialOrgId = pathSegments.pop();

    console.log("CURRENT", currentPotentialTab);

    if (
      currentPotentialTab &&
      ["members", "fees"].includes(currentPotentialTab)
    ) {
      setSelectedTab(currentPotentialTab);
    } else if (
      selectedOrganization &&
      currentPotentialOrgId === selectedOrganization
    ) {
      setSelectedTab("members");
    } else {
      setSelectedTab("members");
    }
  }, [location.pathname, selectedOrganization]);

  const handleOrganizationChange = (
    event: React.ChangeEvent<HTMLSelectElement>,
  ) => {
    setSelectedOrganization(event.target.value);
  };

  return (
    <main className="flex bg-base-100 h-screen">
      <section
        className="
          bg-base-100 
          h-full 
          overflow-y-auto overflow-x-hidden
          flex flex-col flex-nowrap 
          space-y-3 xl:space-y-2
          p-3 text-center 
          xl:p-8 xl:text-left
          w-20 xl:w-1/5
          transition-all duration-300 ease-in-out
          [&_button]:text-sm // Affects text size in buttons
          border-r border-base-300 // Optional: visual separator
        "
      >
        <h2 className="text-xl xl:text-2xl mb-6 xl:mb-20 font-semibold shrink-0 h-10 xl:h-auto flex items-center justify-center xl:justify-start">
          <span className="xl:hidden">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-8 w-8"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fillRule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zm0-2a6 6 0 100-12 6 6 0 000 12zm-3-7a1 1 0 11-2 0 1 1 0 012 0zm2-2a1 1 0 100-2 1 1 0 000 2zm5 0a1 1 0 100-2 1 1 0 000 2zm-2 4a1 1 0 11-2 0 1 1 0 012 0z"
                clipRule="evenodd"
              />
            </svg>
          </span>
          <span className="hidden xl:inline">Dashboard</span>
        </h2>
        {/* Organization Selector */}
        <div className="mb-4 xl:mb-8 shrink-0">
          {/* Icon button for collapsed state - could open a modal or navigate */}
          <button
            className="btn btn-circle btn-accent xl:hidden mx-auto"
            onClick={() => {
              if (!isOrgSelectionDisabled) {
                // Maybe open a modal for selection on small screens
                alert("Open organization selector modal/dropdown");
              }
            }}
            disabled={isOrgSelectionDisabled}
            title={
              isOrgSelectionDisabled
                ? member
                  ? "No organizations"
                  : "Loading..."
                : "Select Organization"
            }
          >
            <FiBriefcase size={18} />
          </button>

          {/* Full select for expanded state */}
          <select
            className="select select-lg !text-sm select-accent rounded-box bg-accent text-neutral-content w-full hidden xl:block"
            value={selectedOrganization || ""}
            onChange={handleOrganizationChange}
            disabled={isOrgSelectionDisabled}
          >
            {!selectedOrganization && isOrgSelectionDisabled && (
              <option value="" disabled>
                {member ? "No organizations" : "Loading organizations..."}
              </option>
            )}
            {!selectedOrganization && !isOrgSelectionDisabled && (
              <option value="" disabled>
                Select Organization
              </option>
            )}
            {member &&
              member.organizationRolesMap &&
              Object.keys(member.organizationRolesMap).map((orgId) => (
                <option key={orgId} value={orgId}>
                  {member.organizationRolesMap![orgId].organizationName}
                </option>
              ))}
          </select>
        </div>

        <DashboardTab
          leadingIcon={<FiUser size={16} />}
          label="Members"
          isSelected={"members" === selectedTab}
          onClick={() => {
            if (selectedOrganization) {
              navigate(
                paths.app.dashboard.members.getHref(selectedOrganization),
              );
            }
          }}
        />
        <DashboardTab
          leadingIcon={<TbCash size={16} />}
          label="Fees"
          isSelected={"fees" === selectedTab}
          onClick={() => {
            if (selectedOrganization) {
              navigate(paths.app.dashboard.fees.getHref(selectedOrganization));
            }
          }}
        />
      </section>
      <section className="bg-base-200 p-8 flex-1 overflow-y-auto">
        {children}
      </section>
    </main>
  );
};

export default DashboardLayout;
