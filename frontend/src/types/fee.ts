export type Fee = {
  memberName: string;
  feeId: number;
  amount: number;
  semester: string;
  year: string;
  dueDate: string;
  datePaid: string | null;
  memberId: number;
};

export type FeeQueryOptions = {
  organizationId?: number | string;
  semester?: number;
  year?: number;
  isPaid?: boolean;
};

export type UseFeesReturn = {
  fees: Fee[] | null;
  options: {
    availableYears: string[];
  };
  pending: boolean;
  error: Error | null;
  setError: React.Dispatch<React.SetStateAction<Error | null>>;
};
