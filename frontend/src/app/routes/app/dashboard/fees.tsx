import Toast from "@/components/core/toast";
import DashboardTitle from "@/features/dashboard/components/dashboard-title";
import { useFees } from "@/features/dashboard/fees/api/get-fees";
import FeeRow from "@/features/dashboard/fees/components/fee-row";
import { FeeQueryOptions } from "@/types/fee";
import { useEffect, useState } from "react";

// TODO: edit options once backend is good
const FeesDashboard = () => {
  const [filters, setFilters] = useState<FeeQueryOptions>(
    {} as FeeQueryOptions
  );

  const { fees, pending, error } = useFees(filters);

  const handleFilterChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;

    let processedValue: string | boolean | undefined = value;
    if (name === "isPaid") {
      processedValue =
        value === "true" ? true : value === "false" ? false : undefined;
    }

    setFilters((prev) => ({ ...prev, [name]: processedValue }));
  };

  useEffect(() => {
    console.log(filters);
  }, [filters]);

  if (pending) <div className="loading loading-spinner"></div>;
  return (
    <>
      <div className="flex justify-between mb-12">
        <DashboardTitle title="Fees" />

        <div className="flex gap-2">
          <select
            name="semester"
            value={filters.semester}
            className="select w-fit border-none rounded-box bg-base-100"
            onChange={handleFilterChange}
          >
            <option>1st</option>
            <option>2nd</option>
            <option>Midyear</option>
          </select>
          <select
            name="academicYear"
            value={filters.academicYear}
            className="select w-fit border-none rounded-box bg-base-100"
            onChange={handleFilterChange}
          >
            <option>2022-2023</option>
            <option>2023-2025</option>
            <option>2025-2025</option>
          </select>
        </div>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>
              <label>
                <input type="checkbox" className="checkbox" />
              </label>
            </th>
            <th>Organization</th>
            <th>Due Date</th>
            <th>Amount</th>
            <th>Is Paid?</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {fees?.map((fee) => (
            <FeeRow
              organizationName={fee.organization.name ?? ""}
              dueDate={fee.dueDate}
              amount={fee.amount}
              isPaid={!!fee.datePaid}
            />
          ))}
        </tbody>
      </table>
      {<Toast type="error" message="Error fetching fees." show={!!error} />}
    </>
  );
};

export default FeesDashboard;
