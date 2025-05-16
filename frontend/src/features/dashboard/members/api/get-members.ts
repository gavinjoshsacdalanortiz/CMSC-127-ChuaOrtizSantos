import { useState, useEffect, useCallback, useMemo } from "react";
import type {
  Member,
  MemberQueryOptions,
  UseMembersReturn,
} from "@/types/member";
import { api } from "@/lib/api-client";
import { getToken } from "@/lib/cookie";

export function useMembers(
  options: MemberQueryOptions = {},
  organizationId: string
): UseMembersReturn {
  const [members, setMembers] = useState<Member[] | null>(null);
  const [pending, setPending] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);
  const [refreshIndex, setRefreshIndex] = useState<number>(0);

  const optionsString = useMemo(() => JSON.stringify(options), [options]);

  useEffect(() => {
    const currentOptions = JSON.parse(optionsString) as MemberQueryOptions;
    console.log("useMembers Effect: Fetching with options ->", currentOptions);

    let isCancelled = false;

    const fetchData = async () => {
      setPending(true);
      setError(null);

      if (!getToken()) return;

      try {
        // Adjust the endpoint as per your Spring Boot controller mapping for users/members
        // e.g., /users, /api/users, /members, /api/members
        const response = await api.get<{ members: Member[] }>( // Or just `Member[]` if your API returns an array directly
          `/membership/${organizationId}/members`, // OR /users - check your backend endpoint

          {
            headers: { Authorization: `Bearer ${getToken()}` },
            params: currentOptions, // Pass the filter options as query parameters
          }
        );

        if (!isCancelled) {
          // If API returns { members: [...] }, use response.members
          // If API returns [...] directly, use response directly
          setMembers(response.members);
        }
      } catch (err) {
        console.error("useMembers: Fetch failed", err);
        if (!isCancelled) {
          setError(
            err instanceof Error ? err : new Error("Failed to fetch members")
          );
          setMembers(null); // Clear members on error
        }
      } finally {
        if (!isCancelled) {
          setPending(false);
        }
      }
    };

    fetchData();

    // Cleanup function
    return () => {
      isCancelled = true;
      console.log("useMembers Effect: Cleanup");
    };
  }, [optionsString, organizationId]); // Re-run effect if options or refreshIndex changes

  const refresh = useCallback(() => {
    setRefreshIndex((prevIndex) => prevIndex + 1);
  }, []);

  return {
    members,
    pending,
    error,
    setError,
    refresh,
  };
}
