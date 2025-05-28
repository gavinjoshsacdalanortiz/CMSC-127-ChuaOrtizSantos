import Toast from "@/components/core/toast";
import DashboardTitle from "@/features/dashboard/components/dashboard-title";
import { useFees } from "@/features/dashboard/fees/api/get-fees";
import FeeRow from "@/features/dashboard/fees/components/fee-row";
import { useAuth } from "@/lib/auth";
import { reverseMap } from "@/lib/utils";
import { FeeQueryOptions } from "@/types/fee";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router";

const FeesDashboard = () => {
  const { member } = useAuth();
  const { orgId: organizationId } = useParams();
  const [filters, setFilters] = useState<FeeQueryOptions>(
    {} as FeeQueryOptions
  );

  const { fees, options, pending, error } = useFees(filters, organizationId!);

  const stMap = new Map<string, number>();
  stMap.set("1st", 1);
  stMap.set("2nd", 2);

  const reversedStMap = reverseMap(stMap);

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

        {member?.organizationRolesMap![organizationId!].role === "ROLE_ADMIN" &&
          organizationId && (
            <Link
              to={`/app/dashboard/${organizationId}/manage`}
              className="btn btn-primary btn-sm"
            >
              Manage Fees
            </Link>
          )}

        <div className="flex gap-2">
          <select
            name="semester"
            value={filters.semester}
            className="select w-fit border-none rounded-box bg-base-100"
            onChange={handleFilterChange}
          >
            <option value="">Semester</option>
            <option value={1}>1st</option>
            <option value={2}>2nd</option>
          </select>
          <select
            name="year"
            value={filters.year}
            className="select w-fit border-none rounded-box bg-base-100"
            onChange={handleFilterChange}
          >
            <option value="">Year</option>
            {options.availableYears.map((year) => (
              <option value={parseInt(year)}>{year}</option>
            ))}
          </select>
        </div>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th></th>
            <th>Semester Issued</th>
            <th>Due Date</th>
            <th>Amount</th>
            <th>Is Paid?</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {fees?.map((fee, index) => (
            <FeeRow
              index={index + 1}
              semesterIssued={
                "S.Y. " +
                fee.year! +
                ", " +
                reversedStMap.get(parseInt(fee.semester)) +
                " semester"
              }
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
