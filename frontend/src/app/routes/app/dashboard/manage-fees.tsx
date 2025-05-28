import React, { useEffect, useState } from "react";
import { useParams } from "react-router";
import DashboardTitle from "@/features/dashboard/components/dashboard-title";
import FeeRow from "@/features/dashboard/fees/components/fee-row";
import { useOrganizationFees } from "@/features/dashboard/fees/api/get-organization-fees"; // Adjust path
import { FeeQueryOptions } from "@/types/fee"; // Assuming Fee type is also in here or imported by FeeRow
import { reverseMap } from "@/lib/utils"; // If needed for semester display

const ManageFeesPage = () => {
  const { orgId } = useParams<{ orgId: string }>();
  const [filters, setFilters] = useState<FeeQueryOptions>(
    {} as FeeQueryOptions
  );

  const { fees, options, pending, error } = useOrganizationFees(
    filters,
    orgId!,
    true
  );

  const stMap = new Map<string, number>([
    ["1st", 1],
    ["2nd", 2],
  ]);
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
    if (orgId) {
      console.log(`Fetching all fees for organization: ${orgId}`);
    }
  }, [orgId]);

  if (!orgId) {
    return <div className="p-6">Organization ID is missing.</div>;
  }

  if (pending) {
    return (
      <div className="p-6">
        <DashboardTitle title={`Manage Fees - ${orgId}`} />
        <div className="flex justify-center items-center h-64">
          <div className="loading loading-spinner loading-lg"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6">
        <DashboardTitle title={`Manage Fees - ${orgId}`} />
        <div className="alert alert-error mt-4">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="stroke-current shrink-0 h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M10 14l2-2m0 0l2-2m-2 2l-2 2m2-2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          <span>Error fetching fees: {error.message}</span>
        </div>
      </div>
    );
  }

  return (
    <div className="p-4 md:p-6">
      <div className="flex justify-between mb-12">
        <DashboardTitle title="Fees" />

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

      <div className="mt-6 overflow-x-auto">
        {fees && fees.length > 0 ? (
          <table className="table table-zebra w-full">
            <thead>
              <tr>
                <th>#</th>
                <th>Member Name</th>
                <th>Semester Issued</th>
                <th>Due Date</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Date Paid</th>
              </tr>
            </thead>
            <tbody>
              {fees?.map((fee, index) => (
                <FeeRow
                  index={index + 1}
                  memberName={fee.memberName}
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
                  datePaid={fee.datePaid}
                />
              ))}
            </tbody>
          </table>
        ) : (
          <div className="text-center py-10">
            <p className="text-xl text-gray-500">
              No fees found for this organization.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ManageFeesPage;
