import React, { useEffect } from "react";
import { useParams } from "react-router";
import DashboardTitle from "@/features/dashboard/components/dashboard-title";
import FeeRow from "@/features/dashboard/fees/components/fee-row";
import { useOrganizationFees } from "@/features/dashboard/fees/api/get-organization-fees"; // Adjust path
import { FeeQueryOptions } from "@/types/fee"; // Assuming Fee type is also in here or imported by FeeRow
import { reverseMap } from "@/lib/utils"; // If needed for semester display

const ManageFeesPage = () => {
  const { orgId } = useParams<{ orgId: string }>();

  const {
    fees,
    pending,
    error,
  } = useOrganizationFees({} as FeeQueryOptions, orgId!, true); // Fetch immediately, enabled=true

  // For displaying semester (1st, 2nd) - if your Fee object stores semester as number
  const stMap = new Map<string, number>([["1st", 1], ["2nd", 2]]);
  const reversedStMap = reverseMap(stMap);

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
          <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2 2m2-2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
          <span>Error fetching fees: {error.message}</span>
        </div>
      </div>
    );
  }

  return (
    <div className="p-4 md:p-6">
      <DashboardTitle title={`All Fees - ${orgId}`} />

      {/* Optional: Display available years if you want to add filters later */}
      {/* {options?.availableYears && options.availableYears.length > 0 && (
        <div className="my-4 p-2 bg-base-200 rounded">
          Available Academic Years: {options.availableYears.join(", ")}
        </div>
      )} */}

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
        ) : (
          <div className="text-center py-10">
            <p className="text-xl text-gray-500">No fees found for this organization.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ManageFeesPage;