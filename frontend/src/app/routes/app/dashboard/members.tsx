import Toast from "@/components/core/toast";
import DashboardTitle from "@/features/dashboard/components/dashboard-title";
import { useMembers } from "@/features/dashboard/members/api/get-members";
import MemberFilterButton from "@/features/dashboard/members/components/member-filter-button";
import MemberRow from "@/features/dashboard/members/components/member-row";
import { reverseMap, toTitleCase } from "@/lib/utils";
import { MemberQueryOptions, StatQueryOptions } from "@/types/member";
import { useEffect, useState } from "react";
import { useParams } from "react-router";

const MembersDashboard = () => {
  const { orgId: organizationId } = useParams();

  const [filters, setFilters] = useState<MemberQueryOptions>(
    {} as MemberQueryOptions,
  );
  const [statOptions, setStatOptions] = useState<StatQueryOptions>(
    {} as StatQueryOptions,
  );

  const { members, stats, options, pending, error } = useMembers(
    filters,
    statOptions,
    organizationId!,
  );

  const stMap = new Map<string, number>();
  stMap.set("1st", 1);
  stMap.set("2nd", 2);

  const reversedStMap = reverseMap(stMap);

  const handleFilterChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFilters((prev) => ({
      ...prev,
      [name]: value === "" ? undefined : value,
    }));
  };

  const handleClearFilter = (name: string) => {
    setFilters((prev) => ({ ...prev, [name]: undefined }));
  };

  const handleStatChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setStatOptions((prev) => ({
      ...prev,
      [name]:
        value === ""
          ? undefined
          : name == "startSemester"
            ? stMap.get(value)
            : parseInt(value),
    }));

    if (statOptions.startSemester === undefined) {
      setStatOptions((prev) => ({
        ...prev,
        [name]:
          value === ""
            ? undefined
            : name == "startSemester"
              ? stMap.get(value)
              : parseInt(value),
      }));
    }
  };

  const handleClearStatOption = (_: string) => {
    setStatOptions({} as StatQueryOptions);
  };

  useEffect(() => {
    console.log(statOptions);
  }, [statOptions]);

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
            {options.availableBatches.map((batch) => (
              <option>{batch}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="flex gap-2">
        <MemberFilterButton
          name="position"
          label="Position"
          activated={!!filters.position}
          options={["President", "Secretary", "Treasurer", "Member"]}
          onChange={handleFilterChange}
          onClear={handleClearFilter}
        />
        <MemberFilterButton
          name="status"
          label="Status"
          activated={!!filters.status}
          options={["active", "inactive", "expelled", "suspended", "alumni"]}
          onChange={handleFilterChange}
          onClear={handleClearFilter}
        />
        <MemberFilterButton
          name="gender"
          label="Gender"
          activated={!!filters.gender}
          options={["Male", "Female"]}
          onChange={handleFilterChange}
          onClear={handleClearFilter}
        />
        <MemberFilterButton
          name="degreeProgram"
          label="Degree Program"
          activated={!!filters.degreeProgram}
          options={options.availableDegreePrograms}
          onChange={handleFilterChange}
          onClear={handleClearFilter}
        />
        <MemberFilterButton
          name="committee"
          label="Committee"
          activated={!!filters.committee}
          options={options.availableCommittees}
          onChange={handleFilterChange}
          onClear={handleClearFilter}
        />

        <div className="join rounded-xl">
          <div className="relative inline-flex items-center join-item [&_select]:!rounded-none">
            <MemberFilterButton
              name="startYear"
              label="Start Year"
              activated={!!statOptions.startYear}
              options={["2025", "2024", "2023", "2022", "2021", "2020"]}
              onChange={handleStatChange}
            />
          </div>
          <div className="relative inline-flex items-center join-item [&_select]:!rounded-none">
            <MemberFilterButton
              name="startSemester"
              label="Start Semester"
              activated={!!statOptions.startSemester}
              options={["1st", "2nd"]}
              onChange={handleStatChange}
              onClear={handleClearStatOption}
            />
          </div>
        </div>
      </div>

      {stats && (
        <div className="flex flex-col mt-6 gap-2 indicator">
          <div className="badge badge-secondary badge-sm indicator-item">
            {"S.Y. " +
              statOptions.startYear! +
              ", " +
              reversedStMap.get(statOptions.startSemester!) +
              " semester up to now"}
          </div>
          <div className="stats bg-base-100  shadow w-fit h-fit">
            {stats.map((stat) => (
              <div className="stat">
                <div className="stat-title">{toTitleCase(stat.status)}</div>
                <div className="stat-value">{stat.percentage.toFixed(2)}%</div>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="mt-4 overflow-x-auto rounded-box border border-base-content/5 bg-base-100">
        <table className="table overflow-y-scroll">
          <thead>
            <tr>
              <th></th>
              <th>Name</th>
              <th>Degree Program</th>
              <th>Batch</th>
              <th>Email</th>
              <th>Gender</th>
              <th>Committee</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {members?.map((member, index) => (
              <MemberRow member={member} index={index + 1} />
            ))}
          </tbody>
        </table>
      </div>
      {<Toast type="error" message="Error fetching fees." show={!!error} />}
    </>
  );
};

export default MembersDashboard;
