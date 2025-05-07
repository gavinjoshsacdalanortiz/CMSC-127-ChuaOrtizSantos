export interface GrantedAuthority {
  authority: string;
}

export interface Member {
  id: string; // UUID maps to string
  firstName: string;
  lastName: string;
  gender: "male" | "female" | string; // Be more specific if possible, or string if values can vary
  degreeProgram: string;
  batch: string;
  email: string;
  createdAt: string; // Date maps to ISO date string
  updatedAt: string; // Date maps to ISO date string
  authorities?: GrantedAuthority[] | string[]; // Assuming an array of role strings or objects
  organizationRolesMap?: Record<string, string[]>; // Map<String, List<String>>
  status?: "active" | "inactive" | "expelled" | "suspended" | "alumni"; // From your filters
  committee?: string; // Added as per your note
  // Other UserDetails fields like isAccountNonExpired are usually not serialized
}

// Define Query Options for Members based on your Filters type
export type MemberStatus =
  | "active"
  | "inactive"
  | "expelled"
  | "suspended"
  | "alumni";
export type MemberGender = "male" | "female";

export interface MemberQueryOptions {
  role?: string;
  status?: MemberStatus;
  gender?: MemberGender | string;
  degreeProgram?: string;
  batch?: string;
  committee?: string;
}

// Define Return type for the hook
export interface UseMembersReturn {
  members: Member[] | null;
  pending: boolean;
  error: Error | null;
  setError: React.Dispatch<React.SetStateAction<Error | null>>;
  refresh: () => void;
}
