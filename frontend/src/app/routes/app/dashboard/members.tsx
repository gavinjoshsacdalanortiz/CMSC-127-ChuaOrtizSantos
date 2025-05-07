import Toast from "@/components/core/toast";
import DashboardTitle from "@/features/dashboard/components/dashboard-title";
import { useMembers } from "@/features/dashboard/members/api/get-members";
import MemberFilterButton from "@/features/dashboard/members/components/member-filter-button";
import { MemberQueryOptions } from "@/types/member";
import { useEffect, useState } from "react";

const MembersDashboard = () => {
  const [filters, setFilters] = useState<MemberQueryOptions>(
    {} as MemberQueryOptions,
  );
  // TODO: show members; use table na rin
  const { members, pending, error } = useMembers(filters);

  const handleFilterChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFilters((prev) => ({
      ...prev,
      [name]: value === "" ? undefined : value,
    }));
  };

  useEffect(() => {
    console.log(filters);
  }, [filters]);

  if (pending) <div className="loading loading-spinner"></div>;

  return (
    <>
      <div className="flex justify-between mb-12">
        <DashboardTitle title="Members" />

        <div className="space-y-1">
          <select
            name="batch"
            value={filters.batch}
            className="select border-none rounded-box bg-base-100"
            onChange={(e) => {
              const newBatch = e.target.value;

              setFilters((prevFilters) => ({
                ...prevFilters,
                batch: newBatch,
              }));
            }}
          >
            <option>2023</option>
            <option>2022</option>
          </select>
        </div>
      </div>

      <div className="flex gap-2">
        <MemberFilterButton
          name="role"
          label="Role"
          activated={!!filters.role}
          options={["test", "test"]}
          onChange={handleFilterChange}
        />
        <MemberFilterButton
          name="status"
          label="Status"
          activated={!!filters.status}
          options={["Active", "Inactive", "Expelled", "Suspended", "Alumni"]}
          onChange={handleFilterChange}
        />
        <MemberFilterButton
          name="gender"
          label="Gender"
          activated={!!filters.gender}
          options={["Male", "Female"]}
          onChange={handleFilterChange}
        />
        <MemberFilterButton
          name="degreeProgram"
          label="Degree Program"
          activated={!!filters.degreeProgram}
          options={["Male", "Female"]}
          onChange={handleFilterChange}
        />
        <MemberFilterButton
          name="committee"
          label="Committee"
          activated={!!filters.committee}
          options={["Male", "Female"]}
          onChange={handleFilterChange}
        />
      </div>
      {<Toast type="error" message="Error fetching fees." show={!!error} />}
    </>
  );
};

export default MembersDashboard;
