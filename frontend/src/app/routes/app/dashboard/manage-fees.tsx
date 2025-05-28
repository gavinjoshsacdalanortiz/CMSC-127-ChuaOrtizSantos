import React, { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router";
import DashboardTitle from "@/features/dashboard/components/dashboard-title";
import FeeRow from "@/features/dashboard/fees/components/fee-row";
import { useOrganizationFees } from "@/features/dashboard/fees/api/get-organization-fees";
import {
  FeeQueryOptions,
  Fee,
  FeeTotals,
  HighestDebtMember,
} from "@/types/fee";
import { reverseMap } from "@/lib/utils";
import { api } from "@/lib/api-client";

const useFeeTotalsInternal = (
  orgId: string,
  filters: { asOfDate?: string; semester?: number; year?: number },
  enabled: boolean
) => {
  const [data, setData] = useState<FeeTotals | null>(null);
  const [pending, setPending] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    if (!orgId || !enabled || !filters.asOfDate) {
      setData(null);
      setPending(false);
      if (enabled && orgId && !filters.asOfDate) {
        setError(new Error("As of Date is required for fee totals."));
      } else {
        setError(null);
      }
      return;
    }
    const fetchTotals = async () => {
      setPending(true);
      setError(null);
      try {
        const response = await api.get<FeeTotals>(`/fees/totals/${orgId}`, {
          params: { asOfDate: filters.asOfDate },
        });
        console.log(response);
        setData(response);
      } catch (err) {
        setError(
          err instanceof Error ? err : new Error("Failed to fetch fee totals")
        );
        setData(null);
      } finally {
        setPending(false);
      }
    };
    fetchTotals();
  }, [orgId, filters.asOfDate, enabled]);

  return { data, pending, error };
};

const useHighestDebtMembersInternal = (
  orgId: string,
  filters: { semester?: number; year?: number },
  enabled: boolean
) => {
  const [data, setData] = useState<HighestDebtMember[] | null>(null);
  const [pending, setPending] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    if (!orgId || !filters.semester || !filters.year || !enabled) {
      setData(null);
      setPending(false);
      if (enabled && orgId && (!filters.semester || !filters.year)) {
        setError(new Error("Semester and Year are required for highest debt."));
      } else {
        setError(null);
      }
      return;
    }
    const fetchHighestDebt = async () => {
      setPending(true);
      setError(null);
      try {
        const responseData = await api.get<any[]>(`/fees/highest-debt`, {
          params: {
            organizationId: orgId,
            semester: filters.semester,
            year: filters.year,
          },
        });

        const mappedData: HighestDebtMember[] = responseData.map((item) => ({
          memberId: item.memberId,
          memberName: `${item.lastName}, ${item.firstName}`,
          totalDebt: item.totalDebt,
          email: item.email,
          gender: item.gender,
          degreeProgram: item.degreeProgram,
        }));
        setData(mappedData);
      } catch (err) {
        setError(
          err instanceof Error
            ? err
            : new Error("Failed to fetch highest debt members")
        );
        setData(null);
      } finally {
        setPending(false);
      }
    };
    fetchHighestDebt();
  }, [orgId, filters.semester, filters.year, enabled]);

  return { data, pending, error };
};

const useLatePaymentsInternal = (
  orgId: string,
  filters: { semester?: number; year?: number },
  enabled: boolean
) => {
  const [data, setData] = useState<Fee[] | null>(null);
  const [pending, setPending] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    if (!orgId || !filters.semester || !filters.year || !enabled) {
      setData(null);
      setPending(false);
      if (enabled && orgId && (!filters.semester || !filters.year)) {
        setError(
          new Error("Semester and Year are required for late payments.")
        );
      } else {
        setError(null);
      }
      return;
    }
    const fetchLatePayments = async () => {
      setPending(true);
      setError(null);
      try {
        const responseData = await api.get<any[]>(`/fees/late-payments`, {
          params: {
            organizationId: orgId,
            semester: filters.semester,
            year: filters.year,
          },
        });
        const mappedData: Fee[] = responseData.map((item) => ({
          id: item.feeId,
          amount: item.amount,
          semester: item.semester,
          year: item.year,
          dueDate: item.dueDate,
          datePaid: item.datePaid,
          organizationId: item.organizationId,
          member: {
            id: item.memberId,
            firstName: item.memberFirstName,
            lastName: item.memberLastName,
            // email: item.memberEmail, // Add to Member type if needed
          },
          memberName: `${item.memberLastName}, ${item.memberFirstName}`, // Pre-construct for FeeRow
          isLate: true, // By definition for this endpoint
        }));
        setData(mappedData);
      } catch (err) {
        setError(
          err instanceof Error
            ? err
            : new Error("Failed to fetch late payments")
        );
        setData(null);
      } finally {
        setPending(false);
      }
    };
    fetchLatePayments();
  }, [orgId, filters.semester, filters.year, enabled]);

  return { data, pending, error };
};

type ManageFeesTab = "allFees" | "latePayments" | "totals" | "highestDebt";

const ManageFeesPage = () => {
  const { orgId } = useParams<{ orgId: string }>();
  const [activeTab, setActiveTab] = useState<ManageFeesTab>("allFees");
  const [filters, setFilters] = useState<FeeQueryOptions>({
    semester: undefined,
    year: undefined,
    asOfDate: undefined,
  } as FeeQueryOptions);

  const commonFeeHookFilters = useMemo(
    () => ({
      semester: filters.semester,
      year: filters.year,
    }),
    [filters.semester, filters.year]
  );

  const {
    fees: allFeesData,
    options: feeOptions,
    pending: allFeesPending,
    error: allFeesError,
  } = useOrganizationFees(
    commonFeeHookFilters,
    orgId!,
    activeTab === "allFees"
  );

  const {
    data: latePaymentsData,
    pending: latePaymentsPending,
    error: latePaymentsError,
  } = useLatePaymentsInternal(
    orgId!,
    { semester: filters.semester, year: filters.year },
    activeTab === "latePayments" && !!filters.semester && !!filters.year
  );

  const {
    data: feeTotalsData,
    pending: totalsPending,
    error: totalsError,
  } = useFeeTotalsInternal(
    orgId!,
    {
      asOfDate: filters.asOfDate,
      semester: filters.semester,
      year: filters.year,
    },
    activeTab === "totals"
  );

  const {
    data: highestDebtData,
    pending: highestDebtPending,
    error: highestDebtError,
  } = useHighestDebtMembersInternal(
    orgId!,
    { semester: filters.semester, year: filters.year },
    activeTab === "highestDebt" && !!filters.semester && !!filters.year
  );

  useEffect(() => {
    if (
      feeOptions?.availableYears?.length > 0 &&
      filters.year === undefined &&
      activeTab !== "totals"
    ) {
      setFilters((prev) => ({
        ...prev,
        semester: prev.semester === undefined ? 1 : prev.semester,
        year: parseInt(feeOptions.availableYears[0], 10),
      }));
    } else if (activeTab === "totals" && filters.asOfDate === undefined) {
      const today = new Date().toISOString().split("T")[0];
      setFilters((prev) => ({ ...prev, asOfDate: today }));
    }
  }, [feeOptions?.availableYears, activeTab]);

  const stMap = new Map<string, number>([
    ["1st", 1],
    ["2nd", 2],
  ]);
  const reversedStMap = reverseMap(stMap);

  const handleFilterChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    let processedValue: string | number | boolean | undefined = value;

    if (name === "isPaid" || name === "isLate") {
      processedValue =
        value === "true" ? true : value === "false" ? false : undefined;
    } else if (name === "semester" || name === "year") {
      processedValue = value === "" ? "" : parseInt(value, 10);
    } else if (name === "asOfDate") {
      processedValue = value === "" ? "" : value;
    }
    setFilters((prev) => ({ ...prev, [name]: processedValue }));
  };

  if (!orgId) {
    return <div className="p-6">Organization ID is missing.</div>;
  }

  const renderMainContent = (
    feesToRender: Fee[] | null,
    pendingState: boolean,
    errorState: Error | null,
    noDataMessage: string
  ) => {
    if (pendingState) {
      return (
        <div className="flex justify-center items-center h-64">
          <div className="loading loading-spinner loading-lg"></div>
        </div>
      );
    }
    if (errorState) {
      return (
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
          <span>Error fetching data: {errorState.message}</span>
        </div>
      );
    }
    return (
      <div className="mt-6 overflow-x-auto">
        {feesToRender && feesToRender.length > 0 ? (
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
              {feesToRender.map((fee, index) => (
                <FeeRow
                  key={fee.feeId || index}
                  index={index + 1}
                  memberName={fee.memberName}
                  semesterIssued={
                    fee.year && fee.semester
                      ? `S.Y. ${fee.year}, ${reversedStMap.get(
                          typeof fee.semester === "string"
                            ? parseInt(fee.semester)
                            : fee.semester
                        )} Sem`
                      : "N/A"
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
            <p className="text-xl text-gray-500">{noDataMessage}</p>
          </div>
        )}
      </div>
    );
  };

  const renderTabContent = () => {
    switch (activeTab) {
      case "allFees":
        return renderMainContent(
          allFeesData,
          allFeesPending,
          allFeesError,
          "No fees found for this organization."
        );
      case "latePayments":
        if (!filters.semester || !filters.year)
          return (
            <p className="text-center p-4">
              Please select a semester and year to view late payments.
            </p>
          );
        return renderMainContent(
          latePaymentsData,
          latePaymentsPending,
          latePaymentsError,
          "No late payments found for the selected criteria."
        );
      case "totals":
        if (!filters.asOfDate)
          return (
            <p className="text-center p-4">
              Please select an 'As of Date' to view totals.
            </p>
          );
        if (totalsPending)
          return <div className="loading loading-spinner mx-auto my-6"></div>;
        if (totalsError)
          return <div className="alert alert-error">{totalsError.message}</div>;
        return (
          <div className="mt-6">
            <h3 className="text-lg font-semibold mb-2">
              Fee Totals as of{" "}
              {new Date(filters.asOfDate!).toLocaleDateString()}
            </h3>
            {feeTotalsData ? (
              <div className="stats shadow w-full md:w-auto">
                <div className="stat">
                  <div className="stat-title">Total Paid</div>
                  <div className="stat-value text-success">
                    ₱{feeTotalsData.totalPaid?.toFixed(2) || "0.00"}
                  </div>
                </div>
                <div className="stat">
                  <div className="stat-title">Total Unpaid</div>
                  <div className="stat-value text-error">
                    ₱{feeTotalsData.totalUnpaid?.toFixed(2) || "0.00"}
                  </div>
                </div>
                <div className="stat">
                  <div className="stat-title">Grand Total (Issued)</div>
                  <div className="stat-value">
                    ₱
                    {(
                      (feeTotalsData.totalPaid || 0) +
                      (feeTotalsData.totalUnpaid || 0)
                    ).toFixed(2)}
                  </div>
                </div>
              </div>
            ) : (
              <p>
                No fee total data available. Ensure 'As of Date' is selected.
              </p>
            )}
          </div>
        );
      case "highestDebt":
        if (!filters.semester || !filters.year)
          return (
            <p className="text-center p-4">
              Please select a semester and year to view highest debt.
            </p>
          );
        if (highestDebtPending)
          return <div className="loading loading-spinner mx-auto my-6"></div>;
        if (highestDebtError)
          return (
            <div className="alert alert-error">{highestDebtError.message}</div>
          );
        return (
          <div className="mt-6">
            <h3 className="text-lg font-semibold mb-2">
              Member(s) with Highest Debt for S.Y. {filters.year},{" "}
              {reversedStMap.get(filters.semester!)} Semester
            </h3>
            {highestDebtData && highestDebtData.length > 0 ? (
              <ul className="list-disc pl-5 bg-base-100 p-4 rounded-box">
                {highestDebtData.map((member) => (
                  <li key={member.memberId} className="my-1">
                    {member.memberName}:{" "}
                    <span className="font-semibold">
                      ₱{member.totalDebt.toFixed(2)}
                    </span>
                  </li>
                ))}
              </ul>
            ) : (
              <p>
                No members with outstanding debt found for this semester, or all
                fees are paid.
              </p>
            )}
          </div>
        );
      default:
        return null;
    }
  };
  const commonSelectClass =
    "select select-bordered w-full sm:w-fit rounded-box bg-base-100";
  const activeFiltersForTab = () => {
    switch (activeTab) {
      case "allFees":
        return ["semester", "year"];
      case "latePayments":
      case "highestDebt":
        return ["semester", "year"];
      case "totals":
        return ["asOfDate"];
      default:
        return [];
    }
  };
  const currentActiveFilters = activeFiltersForTab();

  return (
    <div className="p-4 md:p-6">
      <DashboardTitle title={`Manage Organization Fees - ${orgId}`} />

      <div className="my-6 p-4 bg-base-200 rounded-box shadow">
        <h2 className="text-xl font-semibold mb-3">Filters</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 items-end">
          {currentActiveFilters.includes("semester") && (
            <div className="form-control">
              <label className="label">
                <span className="label-text">Semester</span>
              </label>
              <select
                name="semester"
                value={filters.semester === undefined ? "" : filters.semester}
                className={commonSelectClass}
                onChange={handleFilterChange}
                disabled={!feeOptions?.availableYears?.length}
              >
                <option value="">All Semesters</option>
                <option value={1}>1st Semester</option>
                <option value={2}>2nd Semester</option>
              </select>
            </div>
          )}
          {currentActiveFilters.includes("year") && (
            <div className="form-control">
              <label className="label">
                <span className="label-text">Academic Year</span>
              </label>
              <select
                name="year"
                value={filters.year === undefined ? "" : filters.year}
                className={commonSelectClass}
                onChange={handleFilterChange}
                disabled={!feeOptions?.availableYears?.length}
              >
                <option value="">All Years</option>
                {feeOptions?.availableYears?.map((year) => (
                  <option key={year} value={parseInt(year)}>
                    S.Y. {year}
                  </option>
                ))}
              </select>
            </div>
          )}
          {currentActiveFilters.includes("asOfDate") && (
            <div className="form-control">
              <label className="label">
                <span className="label-text">As of Date</span>
              </label>
              <input
                type="date"
                name="asOfDate"
                value={filters.asOfDate || ""}
                className="input input-bordered w-full sm:w-fit bg-base-100"
                onChange={handleFilterChange}
              />
            </div>
          )}
        </div>
      </div>

      <div role="tablist" className="tabs tabs-lifted tabs-lg">
        {(
          [
            "allFees",
            "latePayments",
            "totals",
            "highestDebt",
          ] as ManageFeesTab[]
        ).map((tab) => (
          <button
            key={tab}
            role="tab"
            className={`tab ${
              activeTab === tab ? "tab-active font-semibold" : ""
            }`}
            onClick={() => setActiveTab(tab)}
            aria-selected={activeTab === tab}
          >
            {tab === "allFees" && "All Fees"}
            {tab === "latePayments" && "Late Payments"}
            {tab === "totals" && "Fee Totals"}
            {tab === "highestDebt" && "Highest Debt"}
          </button>
        ))}
      </div>
      <div className="bg-base-100 p-1 sm:p-6 rounded-b-box rounded-tr-box shadow-md min-h-[200px]">
        {renderTabContent()}
      </div>
    </div>
  );
};

export default ManageFeesPage;
