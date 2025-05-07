import { Organization } from "./organization";

export type Fee = {
  feeId: number;
  amount: number;
  semester: string;
  academicYear: string;
  dueDate: string;
  datePaid: string | null;
  memberId: number;
  organization: Organization;
};

export type FeeQueryOptions = {
  organizationId?: number | string;
  semester?: string;
  academicYear?: string;
  isPaid?: boolean;
};

export type UseFeesReturn = {
  fees: Fee[] | null;
  pending: boolean;
  error: Error | null;
  setError: React.Dispatch<React.SetStateAction<Error | null>>;
  refresh: () => void;
};
