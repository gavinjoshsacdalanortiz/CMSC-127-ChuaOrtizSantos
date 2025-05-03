import MemberFilterButton from "@/features/dashboard/components/member-filter-button";
import { Filters } from "@/types/api";
import { getDayName, getMonthName } from "@/utils/date";
import { useEffect, useState } from "react";

// TODO: edit options once backend is good

const MembersDashboard = () => {
  const today = new Date();

  const [filters, setFilters] = useState<Filters>({} as Filters);

  useEffect(() => {
    console.log(filters);
  }, [filters]);

  return (
    <>
      <div className="flex justify-between mb-12">
        <div className="space-y-1">
          <h2 className="font-semibold">Members</h2>
          <div className="text-xs text-base-content/60">
            <span className="text-secondary font-semibold">
              {getDayName(today.getDay())}
            </span>
            , {today.getDate()} {getMonthName(today.getMonth())}{" "}
            {today.getFullYear()}
          </div>
        </div>

        <div className="space-y-1">
          <select
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
          label="Role"
          activated={!!filters.role}
          options={["test", "test"]}
          onChange={(newRole) => {
            setFilters((prevFilters) => ({
              ...prevFilters,
              role: newRole,
            }));
          }}
        />
        <MemberFilterButton
          label="Status"
          activated={!!filters.status}
          options={["Active", "Inactive", "Expelled", "Suspended", "Alumni"]}
          onChange={(newStatus) => {
            setFilters((prevFilters) => ({
              ...prevFilters,
              status: newStatus as
                | "active"
                | "inactive"
                | "expelled"
                | "suspended"
                | "alumni",
            }));
          }}
        />
        <MemberFilterButton
          label="Gender"
          activated={!!filters.gender}
          options={["Male", "Female"]}
          onChange={(newGender) => {
            setFilters((prevFilters) => ({
              ...prevFilters,
              gender: newGender as "male" | "female",
            }));
          }}
        />
        <MemberFilterButton
          label="Degree Program"
          activated={!!filters.degreeProgram}
          options={["Male", "Female"]}
          onChange={(newDegreeProgram) => {
            setFilters((prevFilters) => ({
              ...prevFilters,
              degreeProgram: newDegreeProgram,
            }));
          }}
        />
        <MemberFilterButton
          label="Committee"
          activated={!!filters.committee}
          options={["Male", "Female"]}
          onChange={(newCommittee) => {
            setFilters((prevFilters) => ({
              ...prevFilters,
              committee: newCommittee,
            }));
          }}
        />
      </div>
    </>
  );
};

export default MembersDashboard;
